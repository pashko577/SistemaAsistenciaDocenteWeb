import { Routes } from '@angular/router';

export const DOCENTE_ROUTES: Routes = [
  {
    path: 'indexDocente',
    loadComponent: () => import('./pages/index-docente/index-docente').then(m => m.IndexDocente)
  },
  {
    path: '',
    redirectTo: 'indexDocente',
    pathMatch: 'full'
  }
];
