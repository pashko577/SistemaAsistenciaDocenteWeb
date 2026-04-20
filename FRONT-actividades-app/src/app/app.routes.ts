import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { NoAuthGuard } from './core/guards/no-auth-guard';

export const routes: Routes = [
  {
    path: 'login',
    canActivate: [NoAuthGuard],
    loadComponent: () => import('./features/auth/pages/login/login').then(m => m.Login)
  },
  {
    path: '',
    canActivate: [authGuard],
    // Aquí usamos un Layout compartido. 
    // Si el Admin y el Usuario tienen diseños muy distintos, 
    // puedes mantener el AdminLayout y que el Guard gestione el resto.
    loadComponent: () => import('./features/admin/layout/admin-layout/admin-layout')
      .then(m => m.AdminLayout),
    children: [
      {
        path: '',
        redirectTo: 'dashboard',
        pathMatch: 'full'
      },
      {
        path: 'dashboard',
        loadComponent: () => import('./features/admin/pages/dashboard/dashboard')
          .then(m => m.Dashboard)
      },
      {
        path: 'asistencia-administrativo',
        loadComponent: () => import('./features/admin/pages/asistencia-administrativos/asistencia-adminsitrativos/asistencia-adminsitrativos')
          .then(m => m.AsistenciaAdministrativosMain)
      },
      {
        path: 'reporte-administrativo',
        loadComponent: () => import('./features/admin/pages/reporte-administrativos/reporte-administrativos')
          .then(m => m.ReporteAdministrativos)
      },
      {
        path: 'pagos',
        loadComponent: () => import('./features/admin/pages/Pagos/pagos/pagos')
          .then(m => m.Pagos)
      },
      {
        path: 'gestion-docente',
        loadComponent: () => import('./features/admin/pages/gestion-docente/gestion-docente')
          .then(m => m.GestionDocente)
      },
      {
        path: 'gestion-administrativo',
        loadComponent: () => import('./features/admin/pages/gestion-administrativo/gestion-administrativo')
          .then(m => m.GestionAdministrativo)
      },
      {
        path: 'gestion-contrato',
        loadComponent: () => import('./features/admin/pages/gestion-contrato/gestion-contrato')
          .then(m => m.GestionContrato)
      },
      {
        path: 'gestion-academica',
        loadComponent: () => import('./features/admin/pages/getion-academica/gestion-academica/gestion-academica')
          .then(m => m.GestionAcademica)
      },
      {
        path: 'asignacion-docente',
        loadComponent: () => import('./features/admin/pages/asignacion-docente/asignacion-docente/asignacion-docente')
          .then(m => m.AsignacionDocente)
      },
      {
        path: 'permisos',
        loadComponent: () => import('./features/admin/pages/gestion-permisos/permisos/permisos')
          .then(m => m.PermisosComponent)
      }
    ]
  },
  {
    path: '**',
    redirectTo: 'login'
  },
];