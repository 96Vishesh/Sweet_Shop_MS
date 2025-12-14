import { Routes } from '@angular/router';
import { AuthComponent } from './features/auth/auth.component';

export const routes: Routes = [
  { path: '', redirectTo: '/auth', pathMatch: 'full' },
  { path: 'auth', component: AuthComponent },
  { path: 'login', redirectTo: '/auth', pathMatch: 'full' },
  { path: 'signup', redirectTo: '/auth', pathMatch: 'full' },
  { path: '**', redirectTo: '/auth' }
];
