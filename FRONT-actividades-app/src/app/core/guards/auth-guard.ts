// core/guards/auth.guard.ts
import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { Token } from '../services/token';

// core/guards/auth.guard.ts
export const authGuard: CanActivateFn = (route, state) => {
  const tokenService = inject(Token);
  const router = inject(Router);

  if (!tokenService.isLogged()) {
    router.navigate(['/login']);
    return false;
  }

  const roles = tokenService.getRoles();
  if (roles.includes('ADMIN')) return true;

  const rutasPermitidas = tokenService.getRoutes();
  
  // Limpiamos la URL actual (quitamos parámetros de consulta como ?id=1)
  const urlActual = state.url.split('?')[0];

  // Verificamos si la URL coincide exactamente o es una sub-ruta permitida
  const tieneAcceso = rutasPermitidas.some(ruta => 
    urlActual === ruta || urlActual.startsWith(ruta + '/')
  );
  
  if (tieneAcceso) return true;

  // FALLBACK: Si no tiene acceso, intentamos mandarlo al dashboard
  // Pero primero verificamos si el dashboard es una de sus rutas permitidas
  const fallback = '/dashboard';
  if (rutasPermitidas.includes(fallback) && urlActual !== fallback) {
    console.warn(`Bloqueado: ${urlActual}. Reintentando Dashboard.`);
    router.navigate([fallback]);
    return false;
  }

  // Si ni siquiera tiene el dashboard, algo anda mal con sus permisos: Logout.
  console.error('Sin rutas válidas asignadas. Cerrando sesión.');
  tokenService.logOut();
  router.navigate(['/login']);
  return false;
};