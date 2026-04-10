import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RegistrarRolRequest } from '../models/RolModule/registrarrol-request';
import { Rol } from '../models/RolModule/rol';

@Injectable({ providedIn: 'root' })
export class RolesService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/roles';

  // NUEVO: Fundamental para el selector de roles del frontend
  obtenerTodos(): Observable<Rol[]> {
    return this.http.get<Rol[]>(this.url);
  }

  registrarRol(dto: RegistrarRolRequest): Observable<Rol> {
    return this.http.post<Rol>(`${this.url}/RegistrarRol`, dto);
  }

  eliminarRol(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/EliminarRol/${id}`);
  }
}
