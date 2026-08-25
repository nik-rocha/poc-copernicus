import { Component, inject, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterUserRequest } from '../../models/register.model';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { faEye } from '@fortawesome/free-solid-svg-icons'
import { faEyeSlash } from '@fortawesome/free-solid-svg-icons'
import { LoginRequest } from '../../models/login.model';
import { Router } from '@angular/router';
import { Organization } from '../../models/organization.model';
import { OrganizationService } from '../../services/organization.service';

@Component({
  imports: [
    FontAwesomeModule,
    FormsModule
  ],
  selector: 'app-login.component',
  styleUrl: './login.component.css',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {

  private authService = inject(AuthService);
  private orgService = inject(OrganizationService);
  private router = inject(Router);

  faEye = faEye
  faEyeSlash = faEyeSlash

  currentLoginPage: string = 'login';
  isLoading: boolean = false;
  showPass: boolean = false;
  selectedOrganizationId: number | null = null;
  errorMessage: string = '';
  successMessage: string = '';

  formData: RegisterUserRequest = {
    fullName: '',
    email: '',
    password: '',
    accessLevel: 'OPERATOR',
    hasOrganization: false,
    corporateName: '',
    registrationCode: '',
    organizationId: null
  }

  loginData: LoginRequest = {
    email: '',
    password: ''
  };

  organizations: Organization[] = [];

  constructor() {}

  async ngOnInit(): Promise<void> {
    try {
      const response = await this.orgService.getAllOrganizations();

      this.organizations = Array.isArray(response) ? response : (response ? [response] : []);
    } catch (e: any) {
      const message = e.response?.data?.message || 'Erro ao carregar a lista de organizações.';
      this.errorMessage = message
    }
  }

  async onLoginSubmit(): Promise<void> {
    this.errorMessage = ''

    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage = 'Preencha todos os campos antes de continuar.';
      return;
    }

    this.isLoading = true;

    try {
      const response = await this.authService.login(this.loginData);
      console.log('Login realizado com sucesso:', response);

      this.router.navigate(['/mainpage']);
    } catch (e: any) {
      console.error('Erro no login:', e);
      const message = e.response?.data?.message || 'E-mail ou senha inválidos.';
      this.errorMessage = message;
    } finally {
      this.isLoading = false;
    }
  }

  async onRegisterSubmit(): Promise<void> {
    this.isLoading = true;
    this.successMessage = '';

    try {
      const result = await this.authService.register(this.formData);
      console.log("Cadastro com sucesso:", result);

      this.currentLoginPage = 'login';
    } catch (e: any) {

      const message = e.response?.data?.message || 'Erro no cadastro.';
      this.errorMessage = message
    } finally {
      this.isLoading = false;
    }
  }

  togglePasswordVisibility(): void {
    this.showPass = !this.showPass;
  }

  setLoginReturnPage(page: string): void {
    this.errorMessage = ''
    this.currentLoginPage = page;
  }

  setLoginPage(page: string): void {
    this.errorMessage = ''

    if (this.currentLoginPage != 'login' && (!this.formData.fullName || !this.formData.email || !this.formData.password)) {
      this.errorMessage = 'Preencha todos os campos antes de continuar.';
      return;
    }

    this.currentLoginPage = page;
  }

  resetAssets(): void {
    this.formData = {
      fullName: '',
      email: '',
      password: '',
      accessLevel: 'OPERATOR',
      hasOrganization: false,
      corporateName: '',
      registrationCode: '',
      organizationId: null
    }

    this.loginData = {
      email: '',
      password: ''
    }
  }
}
