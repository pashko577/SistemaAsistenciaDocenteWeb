import { Injectable } from '@angular/core';

const TOKEN_KEY = 'access_token';
const ROLES_KEY = 'roles';
const DNI_KEY = 'dni';
const ROUTES_KEY = 'rutas_permitidas'; // 

@Injectable({
  providedIn: 'root',
})
export class Token {

  saveUser(dni: string){
    localStorage.setItem(DNI_KEY, dni);
  }

  getUser(): string | null{
    return localStorage.getItem(DNI_KEY);
  }

  saveToken(token: string): void {
    localStorage.setItem(TOKEN_KEY, token);
  }

  getToken(): string | null{
    return localStorage.getItem(TOKEN_KEY);
  }

  saveRoles(roles: string[]){
    localStorage.setItem(ROLES_KEY, JSON.stringify(roles));
  }

  getRoles(): string[]{
    const roles = localStorage.getItem(ROLES_KEY);
    return roles ? JSON.parse(roles) : [];
  }
  saveRoutes(modulos: any[]): void {
  // Extraemos solo la propiedad 'ruta' de cada objeto módulo
  // y nos aseguramos de que todas empiecen con '/' para evitar errores de comparación
  const rutas = modulos.map(m => m.ruta.startsWith('/') ? m.ruta : `/${m.ruta}`);
  localStorage.setItem(ROUTES_KEY, JSON.stringify(rutas));
}

getRoutes(): string[] {
  const rutas = localStorage.getItem(ROUTES_KEY);
  return rutas ? JSON.parse(rutas) : [];
}

  logOut(): void {
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    localStorage.removeItem(DNI_KEY);
    localStorage.removeItem(ROUTES_KEY); // ¡Importante para la seguridad!
  }

  isLogged(): boolean{
    return !! this.getToken();
  }
}
