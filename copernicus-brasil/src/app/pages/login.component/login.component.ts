import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterUserRequest } from '../../models/register.model';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome'
import { faEye } from '@fortawesome/free-solid-svg-icons'
import { faEyeSlash } from '@fortawesome/free-solid-svg-icons'
import { LoginRequest } from '../../models/login.model';
import { Router } from '@angular/router';

@Component({
  imports: [
    FontAwesomeModule,
    FormsModule
  ],
  selector: 'app-login.component',
  styleUrl: './login.component.css',
  templateUrl: './login.component.html',
})
export class LoginComponent {

  private authService = inject(AuthService);
  private router = inject(Router);

  faEye = faEye
  faEyeSlash = faEyeSlash

  currentLoginPage: string = 'login';
  isLoading: boolean = false;
  showPass: boolean = false;
  selectedOrganizationId: number | null = null;

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

  constructor() {}

  async onLoginSubmit(): Promise<void> {
    if (!this.loginData.email || !this.loginData.password) {
      alert('Preencha o e-mail e a senha.');
      return;
    }

    this.isLoading = true;

    try {
      const response = await this.authService.login(this.loginData);
      console.log('Login realizado com sucesso:', response);

      this.router.navigate(['/mainpage']);
    } catch (error: any) {
      console.error('Erro no login:', error);
      const errorMessage = error.response?.data?.message || 'E-mail ou senha inválidos.';
      alert(errorMessage);
    } finally {
      this.isLoading = false;
    }
  }

  async onRegisterSubmit(): Promise<void> {
    this.isLoading = true;

    try {
      const result = await this.authService.register(this.formData);
      console.log("Cadastro com sucesso:", result);

      alert('Cadastro realizado com sucesso.');
      this.currentLoginPage = 'login';
    } catch (error: any) {
      console.error("Erro na requisição:", error);

      const errorMessage = error.response?.data?.message || 'Erro no cadastro.';
      alert(errorMessage)
    } finally {
      this.isLoading = false;
    }
  }

  togglePasswordVisibility(): void {
    this.showPass = !this.showPass;
  }

  setLoginReturnPage(page: string): void {
    this.currentLoginPage = page;
  }

  setLoginPage(page: string): void {
    if (this.currentLoginPage != 'login' && (!this.formData.fullName || !this.formData.email || !this.formData.password)) {
      alert('Preencha todos os campos antes de continuar.');
      return;
    }

    this.currentLoginPage = page;
  }

  organizations = [
    { id: 1, name: 'Copernicus Matriz' },
    { id: 2, name: 'Copernicus Filial SP' },
    { id: 3, name: 'Empresa Parceira' }
  ];

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
  }
}
