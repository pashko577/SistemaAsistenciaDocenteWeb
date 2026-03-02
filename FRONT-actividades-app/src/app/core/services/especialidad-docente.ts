import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { EspecialidadDocenteRequest } from '../models/especialidad-docente-request';
import { Observable } from 'rxjs';
import { EspecialidadDocenteResponse } from '../models/especialidad-docente-response';

@Injectable({
  providedIn: 'root',
})
export class EspecialidadDocenteService {
  private API_URL = 'http://localhost:8080/api/especialidadDocentes';

  constructor(private http: HttpClient) {}

  // POST: Crear
  crear(especialidad: EspecialidadDocenteRequest): Observable<EspecialidadDocenteResponse> {
    return this.http.post<EspecialidadDocenteResponse>(this.API_URL, especialidad);
  }

  // GET: Listar todos (Vital para el select del formulario)
  listar(): Observable<EspecialidadDocenteResponse[]> {
    return this.http.get<EspecialidadDocenteResponse[]>(this.API_URL);
  }

  // GET: Obtener por ID
  obtenerPorId(id: number): Observable<EspecialidadDocenteResponse> {
    return this.http.get<EspecialidadDocenteResponse>(`${this.API_URL}/${id}`);
  }

  // DELETE: Eliminar
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
