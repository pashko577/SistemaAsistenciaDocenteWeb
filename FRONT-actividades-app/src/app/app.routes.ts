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
        path: 'register',
        canActivate: [NoAuthGuard],
        loadComponent: () => import('./features/auth/pages/register/register').then(m => m.Register)
    },

    {
        path: 'admin',
        canActivate: [authGuard, RoleGuard],
        data: { roles: ['ADMIN']},
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
        ]
    },

    {
        path: 'user',
        canActivate: [authGuard, RoleGuard],
        data: { roles: ['USER']},
        loadComponent: () => import('./features/user/layout/user-layout/user-layout')
            .then(m => m.UserLayout),
        children: [
            {
                path: '',
                redirectTo: 'index-user',
                pathMatch: 'full'
            },
            {
                path: 'index-user',
                loadComponent: () => import('./features/user/pages/index-user/index-user')
                    .then(m => m.IndexUser)
            },
        ]
    },



    {
        path: '**',
        redirectTo: 'login'
    },
];
