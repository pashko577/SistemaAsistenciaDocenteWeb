import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { ModuloResponse } from '../models/RolModule/modulo-response';
import { ModuloRequest } from '../models/RolModule/modulo-request';

@Injectable({ providedIn: 'root' })
export class ModulosService {
  private http = inject(HttpClient);
  private url = 'http://localhost:8080/api/modulos';

  crear(dto: ModuloRequest): Observable<ModuloResponse> {
    return this.http.post<ModuloResponse>(this.url, dto);
  }

  listar(): Observable<ModuloResponse[]> {
    return this.http.get<ModuloResponse[]>(this.url);
  }

  obtenerPorId(id: number): Observable<ModuloResponse> {
    return this.http.get<ModuloResponse>(`${this.url}/${id}`);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
