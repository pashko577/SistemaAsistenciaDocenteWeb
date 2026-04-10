import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { RolModuloResponse } from '../models/RolModule/rolmodulo-response';
import { RolModuloRequest } from '../models/RolModule/rolmodulo-request';

@Injectable({ providedIn: 'root' })
export class RolModuloService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/rol-modulos';

  // Obtiene los módulos que ya tiene asignados un rol
  listarPorRol(rolId: number): Observable<RolModuloResponse[]> {
    return this.http.get<RolModuloResponse[]>(`${this.url}/rol/${rolId}`);
  }

  // Activa un permiso (Switch ON)
  asignarModulo(dto: RolModuloRequest): Observable<RolModuloResponse> {

    return this.http.post<RolModuloResponse>(this.url, dto);
  }

  // Desactiva un permiso (Switch OFF)
  desasignarModulo(rolId: number, moduloId: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/rol/${rolId}/modulo/${moduloId}`);
  }
}
