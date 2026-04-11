import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ModuloResponse } from '../models/RolModule/modulo-response';
import { ModuloRequest } from '../models/RolModule/modulo-request';


@Injectable({ providedIn: 'root' })
export class ModuloService {
  private readonly URL = 'http://localhost:8080/api/modulos';

  constructor(private http: HttpClient) {}

  listar(): Observable<ModuloResponse[]> {
    return this.http.get<ModuloResponse[]>(this.URL);
  }

  crear(dto: ModuloRequest): Observable<ModuloResponse> {
    return this.http.post<ModuloResponse>(this.URL, dto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`);
  }
}
