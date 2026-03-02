import { Injectable } from '@angular/core';

const TOKEN_KEY = 'access_token';
const ROLES_KEY = 'roles';
const DNI_KEY = 'dni';

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

  removeToken(): void{
    localStorage.removeItem(TOKEN_KEY);
    localStorage.removeItem(ROLES_KEY);
    localStorage.removeItem(DNI_KEY);
  }

  isLogged(): boolean{
    return !! this.getToken();
  }
}
