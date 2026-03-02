import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SedeRequest } from '../models/sede-request';
import { SedeResponse } from '../models/sede-response';

@Injectable({
  providedIn: 'root',
})
export class SedeService {
  private API_URL = 'http://localhost:8080/api/sedes';

  constructor(private http: HttpClient) {}

  // POST: Crear Sede
  crearSede(dto: SedeRequest): Observable<SedeResponse> {
    return this.http.post<SedeResponse>(this.API_URL, dto);
  }

  // GET: Listar todas (Para tu select en el formulario)
  listarSedes(): Observable<SedeResponse[]> {
    return this.http.get<SedeResponse[]>(this.API_URL);
  }

  // GET: Obtener una por ID
  obtenerSede(id: number): Observable<SedeResponse> {
    return this.http.get<SedeResponse>(`${this.API_URL}/${id}`);
  }

  // PUT: Actualizar (Tu back recibe un String directamente en el Body)
  actualizarSede(id: number, nombre: string): Observable<SedeResponse> {
    return this.http.put<SedeResponse>(`${this.API_URL}/${id}`, nombre);
  }

  // DELETE: Eliminar
  eliminarSede(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
