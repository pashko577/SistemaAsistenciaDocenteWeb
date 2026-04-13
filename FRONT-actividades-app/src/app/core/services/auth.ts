import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Token } from './token';
import { Router } from '@angular/router';
import { Rol } from './../models/RolModule/rol';

@Injectable({
  providedIn: 'root',
})
export class Auth {
  private API_URL = 'http://localhost:8080/api/auth';

  constructor(
    private http: HttpClient,
    private tokenService: Token,
    private router: Router
  ){}

  login(data: {dni: string; password: string}): Observable<any> {
    return this.http.post(`${this.API_URL}/login`, data);
  }

  register(data: any): Observable<void>{
    return this.http.post<void>(`${this.API_URL}/register`, data);
  }

  logout(): void{
    this.tokenService.logOut();
    this.router.navigate(['/login']);
  }

  getRole(): string | null {
  const token = this.tokenService.getToken();
  if (!token) return null;
  
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // Como en tu token es "roles": ["ADMIN"], accedemos al primer elemento
    return (payload.roles && payload.roles.length > 0) ? payload.roles[0] : null;
  } catch (e) {
    console.error("Error al decodificar el token", e);
    return null;
  }
}

// auth.service.ts
hasRole(targetRole: string): boolean {
  const currentRole = this.getRole(); // Ej: "ROLE_ADMIN"
  if (!currentRole) return false;

  // Lógica de jerarquía manual para el Frontend
  if (currentRole === 'ROLE_ADMIN') return true; 
  
  // Si no es admin, debe ser exactamente el rol solicitado
  return currentRole === targetRole;
}

getRolId(): number | null {
  const token = this.tokenService.getToken();
  if (!token) return null;
  
  try {
    const payload = JSON.parse(atob(token.split('.')[1]));
    // Asegúrate de que el backend esté enviando 'rolId' o 'id' en el payload
    return payload.rolId || null; 
  } catch (e) {
    return null;
  }
}
}
