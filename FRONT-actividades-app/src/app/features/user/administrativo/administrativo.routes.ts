import { Routes } from '@angular/router';

export const ADMINISTRATIVO_ROUTES: Routes = [
  {
    path: 'indexAdministrativo',
    loadComponent: () => import('./pages/index-administrativo/index-administrativo').then(m => m.IndexAdministrativo)
  },
  {
    path: '',
    redirectTo: 'indexAdministrativo',
    pathMatch: 'full'
  }
];
