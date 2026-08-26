import { Component, inject, OnInit, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { RegisterUserRequest } from '../../models/register.model';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { faEye } from '@fortawesome/free-solid-svg-icons';
import { faEyeSlash } from '@fortawesome/free-solid-svg-icons';
import { LoginRequest } from '../../models/login.model';
import { Router } from '@angular/router';
import { Organization } from '../../models/organization.model';
import { OrganizationService } from '../../services/organization.service';

@Component({
  imports: [FontAwesomeModule, FormsModule],
  selector: 'app-login.component',
  styleUrl: './login.component.css',
  templateUrl: './login.component.html',
})
export class LoginComponent implements OnInit {
  private authService = inject(AuthService);
  private orgService = inject(OrganizationService);
  private router = inject(Router);

  faEye = faEye;
  faEyeSlash = faEyeSlash;

  currentLoginPage = signal<string>('login');
  isLoading = signal(false);
  showPass = signal(false);
  selectedOrganizationId: number | null = null;
  returnFromRegister: boolean = false;
  errorMessage = signal('');
  successMessage = signal('');

  formData: RegisterUserRequest = {
    fullName: '',
    email: '',
    password: '',
    accessLevel: 'OPERATOR',
    hasOrganization: false,
    corporateName: '',
    registrationCode: '',
    organizationId: null,
  };

  loginData: LoginRequest = {
    email: '',
    password: '',
  };

  organizations: Organization[] = [];

  constructor() {}

  async ngOnInit(): Promise<void> {
    try {
      const response = await this.orgService.getAllOrganizations();

      this.organizations = Array.isArray(response) ? response : response ? [response] : [];
    } catch (e: any) {
      const message = e.response?.data?.message || 'Erro ao carregar a lista de organizações.';
      this.errorMessage.set(message);
    }
  }

  async onLoginSubmit(): Promise<void> {
    this.errorMessage.set('');
    this.successMessage.set('');

    if (!this.loginData.email || !this.loginData.password) {
      this.errorMessage.set('Preencha todos os campos antes de continuar.');
      return;
    }

    this.isLoading.set(true);

    try {
      const response = await this.authService.login(this.loginData);
      console.log('Login realizado com sucesso:', response);

      this.router.navigate(['/mainpage']);
    } catch (e: any) {
      console.error('Erro no login:', e);
      const message = e.response?.data?.message || 'E-mail ou senha inválidos.';
      this.errorMessage.set(message);
    } finally {
      this.isLoading.set(false);
    }
  }

  async onRegisterSubmit(): Promise<void> {
    this.returnFromRegister = true;
    this.successMessage.set('');
    this.errorMessage.set('');

    if (this.formData.hasOrganization) {
      if (!this.formData.organizationId) {
        this.errorMessage.set('Selecione uma organização da lista.');
        return;
      }
    } else {
      if (!this.formData.corporateName) {
        this.errorMessage.set('Preencha o nome da organização.');
        return;
      }

      const cnpjError = this.validateCnpj(this.formData.registrationCode);
      if (cnpjError) {
        this.errorMessage.set(cnpjError);
        return;
      }
    }

    this.isLoading.set(true);

    try {
      await this.authService.register(this.formData);
      this.successMessage.set('Cadastro realizado com sucesso! Faça login para continuar.');
      this.currentLoginPage.set('login');
    } catch (e: any) {
      const message = e.response?.data?.message || 'Erro no cadastro.';
      this.errorMessage.set(message);
    } finally {
      this.isLoading.set(false);
    }
  }

  togglePasswordVisibility(): void {
    this.showPass.set(!this.showPass());
  }

  setLoginReturnPage(page: string): void {
    this.errorMessage.set('');
    this.successMessage.set('');
    this.currentLoginPage.set(page);
  }

  setLoginPage(page: string): void {
    this.errorMessage.set('');

    if (this.currentLoginPage() != 'login') {
      if (!this.formData.fullName) {
        this.errorMessage.set('Preencha o nome completo.');
        return;
      }

      const emailErr = this.validateEmail(this.formData.email);
      if (emailErr) {
        this.errorMessage.set(emailErr);
        return;
      }

      const passwordErr = this.validatePassword(this.formData.password);
      if (passwordErr) {
        this.errorMessage.set(passwordErr);
        return;
      }
    }

    this.currentLoginPage.set(page);
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
      organizationId: null,
    };

    this.loginData = {
      email: '',
      password: '',
    };
  }

  formatCnpj(value: string): string {
    const digits = value.replace(/\D/g, '').slice(0, 14);
    let result = '';
    if (digits.length > 0) result = digits.slice(0, 2);
    if (digits.length > 2) result += '.' + digits.slice(2, 5);
    if (digits.length > 5) result += '.' + digits.slice(5, 8);
    if (digits.length > 8) result += '/' + digits.slice(8, 12);
    if (digits.length > 12) result += '-' + digits.slice(12, 14);
    return result;
  }

  onCnpjInput(value: string): void {
    this.formData.registrationCode = this.formatCnpj(value);
    this.errorMessage.set('');
  }

  private validateCnpj(cnpj: string | undefined): string | null {
    if (!cnpj || !/^\d{2}\.\d{3}\.\d{3}\/\d{4}-\d{2}$/.test(cnpj)) {
      return 'Preencha o CNPJ da organização (formato xx.xxx.xxx/xxxx-xx, apenas números).';
    }
    return null;
  }

  private validateEmail(email: string): string | null {
    if (!email) {
      return 'Preencha o e-mail.';
    }
    if (email !== email.toLowerCase()) {
      return 'O e-mail não pode conter letras maiúsculas.';
    }
    if (!/^[a-z0-9._%+-]+@[a-z0-9.-]+\.com$/.test(email)) {
      return 'O e-mail deve ser válido e terminar em ".com".';
    }
    return null;
  }

  private validatePassword(password: string): string | null {
    if (!password) {
      return 'Preencha a senha.';
    }
    if (password.length <= 6) {
      return 'A senha deve possuir mais de 6 caracteres.';
    }
    if (!/\d/.test(password)) {
      return 'A senha deve conter pelo menos um número.';
    }
    if (!/[^A-Za-z0-9]/.test(password)) {
      return 'A senha deve conter pelo menos um símbolo.';
    }
    return null;
  }
}
