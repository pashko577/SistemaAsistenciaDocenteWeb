import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { RoleGuard } from './core/guards/role-guard';
import { NoAuthGuard } from './core/guards/no-auth-guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [NoAuthGuard],
    loadComponent: () => import('./features/auth/pages/login/login').then(m => m.Login)
  },
  {
    path: 'admin',
    canActivate: [authGuard, RoleGuard],
    data: { roles: ['ADMIN'] },
    loadComponent: () => import('./features/admin/layout/admin-layout/admin-layout')
      .then(m => m.AdminLayout),
    children: [
      {
        path: '',
        redirectTo: 'index-admin',
        pathMatch: 'full'
      },
      {
        path: 'index-admin',
        loadComponent: () => import('./features/admin/pages/index-admin/index-admin')
          .then(m => m.IndexAdmin)
      },
      {
        path: 'gestionDocente',
        loadComponent: () => import('./features/admin/pages/gestion-docente/gestion-docente')
          .then(m => m.GestionDocente)
      },
      {
        path: 'gestionAdministrativo',
        loadComponent: () => import('./features/admin/pages/gestion-administrativo/gestion-administrativo')
          .then(m => m.GestionAdministrativo)
      }
    ]
  },

  {
    path: 'user',
    // Protegemos el acceso general para que solo usuarios logueados carguen el Layout
    canActivate: [authGuard],
    loadComponent: () => import('./features/user/shared/layout/user-layout/user-layout')
      .then(m => m.UserLayout),
    children: [
      {
        path: 'administrativo',
        canActivate: [RoleGuard], // El authGuard ya se validó arriba
        data: { roles: ['ADMINISTRATIVO'] },
        loadChildren: () => import('./features/user/administrativo/administrativo.routes')
          .then(m => m.ADMINISTRATIVO_ROUTES)
      },
      {
        path: 'docente',
        canActivate: [RoleGuard],
        data: { roles: ['DOCENTE'] },
        loadChildren: () => import('./features/user/docente/docente.routes')
          .then(m => m.DOCENTE_ROUTES)
      }
    ]
  },

  {
    path: '**',
    redirectTo: 'login'
  },
];
