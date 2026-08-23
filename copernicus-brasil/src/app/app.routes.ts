import { Routes } from '@angular/router';
import { LoginComponent } from './pages/login.component/login.component';
import { MainCollaboratorsComponent } from './pages/main-collaborators.component/main-collaborators.component';

export const routes: Routes = [
  { path: '', component: LoginComponent},
  { path: 'mainpage', component: MainCollaboratorsComponent}
];
