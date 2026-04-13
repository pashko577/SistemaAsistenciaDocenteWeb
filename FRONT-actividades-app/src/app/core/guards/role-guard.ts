/* import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, GuardResult, MaybeAsync, Route, Router, RouterStateSnapshot } from '@angular/router';
import { Token } from '../services/token';
@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private tokenService: Token, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
    // 1. Obtenemos las rutas permitidas que guardamos al cargar el sidebar
    const permisosRaw = localStorage.getItem('rutas_permitidas');
    const rutasPermitidas: string[] = permisosRaw ? JSON.parse(permisosRaw) : [];

    // 2. Verificamos si la ruta actual (state.url) coincide con algún permiso
    // state.url nos da la ruta completa que el usuario escribió
    const tienePermiso = rutasPermitidas.some(ruta => state.url.startsWith(ruta));

    if (!tienePermiso) {
      // Si no tiene permiso, lo mandamos a un dashboard genérico o error
      this.router.navigate(['/user/dashboard']);
      return false;
    }

    return true;
  }
} */