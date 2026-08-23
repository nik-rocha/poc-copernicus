import { Component, inject } from '@angular/core';
import { OrganizationService } from '../../services/organization.service';
import { Organization } from '../../models/organization.model';
import { OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { signal } from '@angular/core';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { faHardDrive, faUserGroup, faBuilding, faRightToBracket, faGear, faCirclePlus } from '@fortawesome/free-solid-svg-icons';

@Component({
  standalone: true,
  imports: [
    CommonModule,
    FontAwesomeModule
  ],
  selector: 'app-main-collaborators.component',
  styleUrl: './main-collaborators.component.css',
  templateUrl: './main-collaborators.component.html',
})
export class MainCollaboratorsComponent implements OnInit {

  private orgService = inject(OrganizationService);
  organization = signal<Organization | null>(null);
  isLoading = signal(true);

  faHardDrive = faHardDrive
  faUserGroup = faUserGroup
  faBuilding = faBuilding
  faRightToBracket = faRightToBracket
  faGear = faGear
  faCirclePlus = faCirclePlus

  async ngOnInit(): Promise<void> {
    try {
      const data = await this.orgService.getCurrentOrganization();
      this.organization.set(data);
    } catch (error) {
      console.error(error);
    } finally {
      this.isLoading.set(false);
    }
  }
}
