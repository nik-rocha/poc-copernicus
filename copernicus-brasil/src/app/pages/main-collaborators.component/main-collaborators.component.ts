import { Component, computed, inject } from '@angular/core';
import { OrganizationService } from '../../services/organization.service';
import { Organization, Collaborator, Device } from '../../models/organization.model';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { signal } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { faHardDrive, faUserGroup, faBuilding, faRightToBracket, faGear, faCirclePlus, faFileCircleXmark } from '@fortawesome/free-solid-svg-icons';
import { CollaboratorService } from '../../services/collaborator.service';
import { DeviceService } from '../../services/device.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

type ModalAction = 'create' | 'edit' | 'delete';
type ModalEntity = 'collaborator' | 'device' | 'organization';

interface ModalState {
  entity: ModalEntity;
  action: ModalAction;
}

@Component({
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule,
    FormsModule
  ],
  selector: 'app-main-collaborators.component',
  styleUrl: './main-collaborators.component.css',
  templateUrl: './main-collaborators.component.html',
})
export class MainCollaboratorsComponent implements OnInit {

  private orgService = inject(OrganizationService);
  private collaboratorService = inject(CollaboratorService);
  private deviceService = inject(DeviceService);
  private router = inject(Router);

  organization = signal<Organization | null>(null);
  userOrganization = signal<Organization | null>(null);
  selectedOrganization = signal<Organization | null>(null);
  organizations = signal<Organization[]>([]);
  allCollaborators = signal<Collaborator[]>([]);
  allDevices = signal<Device[]>([]);
  userAccessLevel = signal<string>('OPERATOR');
  isManager = computed(() => this.userAccessLevel() === 'MANAGER');
  searchTerm = signal('');
  modalState = signal<ModalState | null>(null);
  modalTarget = signal<Collaborator | Device | Organization | null>(null);
  editingCollaboratorEmail: string | null = null;
  errorMessage = signal('');

  collaboratorForm: Partial<Collaborator> & { organizationId?: number } = {
    fullName: '', email: '', password: '', accessLevel: 'OPERATOR'
  };
  deviceForm: Partial<Device> & { organizationId?: number } = { model: '', assetTag: '' };
  organizationForm: Partial<Organization> = { corporateName: '', registrationCode: '' };

  currentUser = computed(() => {
    const token = localStorage.getItem('token');
    if (!token) return null;

    try {
      const payloadBase64 = token.split('.')[1];
      const decodedPayload = JSON.parse(atob(payloadBase64));
      const userAccessLevel = decodedPayload.sub;

      return this.allCollaborators().find(c => c.email === userAccessLevel) || null;
    } catch (e: any) {
      const message = e.response?.data?.message || 'Erro ao decodificar o token para conseguir o usuário atual.';
      alert(message)
      return null;
    }
  });

  collaborators = computed(() => {
    const targetOrg = (this.isManager() ? this.selectedOrganization() : this.userOrganization()) as any;

    if (!targetOrg) {
      return this.allCollaborators();
    }

    const targetId = targetOrg.idOrganization;
    return this.allCollaborators().filter((c: any) => c.organizationId === targetId);
  });

  filteredCollaborators = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.collaborators();

    return this.collaborators().filter((c: any) =>
      c.fullName?.toLowerCase().includes(term) ||
      c.email?.toLowerCase().includes(term)
    );
  });

  devices = computed(() => {
    const targetOrg = (this.isManager() ? this.selectedOrganization() : this.userOrganization()) as any;

    if (!targetOrg) {
      return this.allDevices();
    }

    const targetId = targetOrg.idOrganization;
    return this.allDevices().filter((d: any) => d.organizationId === targetId);
  });

  filteredDevices = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.devices();

    return this.devices().filter((d: any) =>
      d.model?.toLowerCase().includes(term) ||
      d.assetTag?.toLowerCase().includes(term)
    );
  });

  filteredOrganizations = computed(() => {
    const term = this.searchTerm().toLowerCase().trim();
    if (!term) return this.organizations();

    return this.organizations().filter((o: any) =>
      o.corporateName?.toLowerCase().includes(term) ||
      o.registrationCode?.toLowerCase().includes(term)
    );
  });

  isLoading = signal(true);
  orgView = 'devices';

  faHardDrive = faHardDrive
  faUserGroup = faUserGroup
  faBuilding = faBuilding
  faRightToBracket = faRightToBracket
  faGear = faGear
  faCirclePlus = faCirclePlus
  faFileCircleXmark = faFileCircleXmark

  private loadUserAccessLevel(): void {
    const token = localStorage.getItem('token');
    if (token) {
      try {
        const payloadBase64 = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payloadBase64));

        const userEmail = decodedPayload.sub;

        const currentUser = this.allCollaborators().find(c => c.email === userEmail);

        if (currentUser && currentUser.accessLevel) {
          this.userAccessLevel.set(currentUser.accessLevel.toUpperCase());
        } else {
          this.userAccessLevel.set('OPERATOR');
        }

      } catch (e:any) {
        const message = e.response?.data?.message || 'Erro ao ler as permissões do token.';
        alert(message)
      }
    }
  }

  displayedOrganization = computed(() =>
    this.isManager() ? (this.selectedOrganization() ?? this.userOrganization()) : this.userOrganization()
  );

  async ngOnInit(): Promise<void> {
    try {
      const [orgsResponse, currentOrg, collabData, deviceData] = await Promise.all([
        this.orgService.getAllOrganizations(),
        this.orgService.getCurrentOrganization(),
        this.collaboratorService.getAll(),
        this.deviceService.getAll()
      ]);

      const orgsData = Array.isArray(orgsResponse) ? orgsResponse : (orgsResponse ? [orgsResponse] : []);

      this.organizations.set(orgsData);
      this.allCollaborators.set(collabData);
      this.allDevices.set(deviceData);

      this.organization.set(currentOrg);
      this.userOrganization.set(currentOrg);

      this.loadUserAccessLevel();

      this.selectedOrganization.set(this.isManager() ? null : this.userOrganization());

    } catch (e: any) {
      const message = e.response?.data?.message || 'Erro ao carregar os dados.';
      alert(message)
    } finally {
      this.isLoading.set(false);
    }
  }

  selectOrganization(org: Organization): void {
    this.selectedOrganization.set(org);
    this.orgView = 'devices';
  }

  changeView(view: string): void {
    this.orgView = view;

      if (view === 'organizations') {
        this.resetOrg()
    }
  }

  logout(): void {
    localStorage.removeItem('token');
    this.router.navigate(['/']);
  }

  onSearchChange(value: string): void {
    this.searchTerm.set(value);
  }

  openModal(entity: ModalEntity, action: ModalAction, target?: any): void {
  this.modalState.set({ entity, action });
  this.modalTarget.set(target ?? null);
  this.errorMessage.set('')

  if (action === 'edit' && target) {
    if (entity === 'collaborator') this.collaboratorForm = { ...target };
    if (entity === 'device') this.deviceForm = { ...target };
    if (entity === 'organization') this.organizationForm = { ...target };
  }

  if (action === 'create') {
    const targetOrgId = this.selectedOrganization()?.idOrganization ?? this.userOrganization()?.idOrganization
    if (entity === 'collaborator') {;
      this.collaboratorForm = { fullName: '', email: '', password: '', accessLevel: 'OPERATOR', organizationId: targetOrgId }
    };
    if (entity === 'device') this.deviceForm = { model: '', assetTag: '', organizationId: targetOrgId };
    if (entity === 'organization') this.organizationForm = { corporateName: '', registrationCode: '' };
  }

  if (entity === 'collaborator' && action === 'edit') {
    this.editingCollaboratorEmail = target.email;
  } else {
    this.editingCollaboratorEmail = null;
  }
}

closeModal(): void {
  this.errorMessage.set('')
  this.modalState.set(null);
  this.modalTarget.set(null);
}

async confirmAction(): Promise<void> {
  const state = this.modalState();
  if (!state) return;

  const { entity, action } = state;
  const targetId = (this.modalTarget() as any)?.idCollaborator
    ?? (this.modalTarget() as any)?.idDevice
    ?? (this.modalTarget() as any)?.idOrganization;

  try {
    let createdId: number | undefined;

    if (entity === 'collaborator') {
      if (action === 'create') {
        const created = await this.collaboratorService.create(this.collaboratorForm);
        createdId = created.idCollaborator;
      }
      if (action === 'edit') {
        const isSelf = this.currentUser()?.email === this.editingCollaboratorEmail;
        const emailChanged = this.editingCollaboratorEmail !== this.collaboratorForm.email;

        await this.collaboratorService.update(targetId!, this.collaboratorForm);

        if (isSelf && emailChanged) {
          alert("Como você mudou seu e-mail, logue no sistema novamente para confirmar a mudança.");
          this.logout();
          return;
        }
      }
      if (action === 'delete') {
        await this.collaboratorService.delete(targetId!);
      }
    }

    if (entity === 'device') {
      if (action === 'create') {
        const created = await this.deviceService.create(this.deviceForm);
        createdId = created.idDevice;
      }
      if (action === 'edit') await this.deviceService.update(targetId!, this.deviceForm);
      if (action === 'delete') await this.deviceService.delete(targetId!);
    }

    if (entity === 'organization') {
      if (action === 'create') {
        const created = await this.orgService.create(this.organizationForm);
        createdId = created.idOrganization;
      }
      if (action === 'edit') await this.orgService.update(targetId!, this.organizationForm);
      if (action === 'delete') await this.orgService.delete(targetId!);
    }

    this.closeModal();
    await this.reloadData();

    if (action === 'create' && createdId) {
      this.scrollToItem(entity, createdId);
    }

  } catch (e: any) {
    const message = e.response?.data?.message || 'Erro ao processar a ação.';
    alert(message);
  }
}

  private scrollToItem(entity: ModalEntity, id: number): void {
    const prefix = entity === 'collaborator' ? 'collaborator' : entity === 'device' ? 'device' : 'organization';

    setTimeout(() => {
      const el = document.querySelector(`[data-id="${prefix}-${id}"]`);
      el?.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }, 0);
  }

    private async reloadData(): Promise<void> {
    const [collabData, deviceData, orgsResponse, currentOrg] = await Promise.all([
      this.collaboratorService.getAll(),
      this.deviceService.getAll(),
      this.orgService.getAllOrganizations(),
      this.orgService.getCurrentOrganization()
    ]);

    this.allCollaborators.set(collabData);
    this.allDevices.set(deviceData);

    const orgsData = Array.isArray(orgsResponse) ? orgsResponse : (orgsResponse ? [orgsResponse] : []);
    this.organizations.set(orgsData);

    this.userOrganization.set(currentOrg);
    this.organization.set(currentOrg);
  }

  resetOrg(): void {
    this.selectedOrganization.set(null);
  }
}
