import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login.component/login.component';
import { MainCollaboratorsComponent } from './pages/main-collaborators.component/main-collaborators.component';
import { authGuard } from './guards/auth-guard';

export const routes: Routes = [
  { path: '', component: LoginComponent},
  { path: 'mainpage', component: MainCollaboratorsComponent, canActivate: [authGuard]}
];
