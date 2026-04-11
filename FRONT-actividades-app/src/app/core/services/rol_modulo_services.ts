import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { RolModuloResponse } from '../models/RolModule/rolmodulo-response';
import { RolModuloRequest } from '../models/RolModule/rolmodulo-request';

@Injectable({ providedIn: 'root' })
export class RolModuloService {
  private readonly URL = 'http://localhost:8080/api/rol-modulos';

  constructor(private http: HttpClient) {}

  listarPorRol(rolId: number): Observable<RolModuloResponse[]> {
    return this.http.get<RolModuloResponse[]>(`${this.URL}/rol/${rolId}`);
  }

  asignar(dto: RolModuloRequest): Observable<RolModuloResponse> {
    return this.http.post<RolModuloResponse>(this.URL, dto);
  }

  desasignar(rolId: number, moduloId: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/rol/${rolId}/modulo/${moduloId}`);
  }
}
