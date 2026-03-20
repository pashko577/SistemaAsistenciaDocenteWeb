
import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TipoActividadResponse } from '../models/Contratos/tipo-actividad-response';
import { TipoPlanilla } from '../models/enums/contrato-enums';
import { TipoActividadRequest } from '../models/Contratos/tipo-actividad-request';


@Injectable({
  providedIn: 'root'
})
export class TipoActividadService {
  // Ajusta la URL según tu controlador de Spring Boot
  private readonly API_URL = 'http://localhost:8080/api/tipo-actividad';

  constructor(private http: HttpClient) {}

  // Listar todos los tipos (Ej: "Docente Primaria", "Auxiliar", "Secretaría")
  listar(): Observable<TipoActividadResponse[]> {
    return this.http.get<TipoActividadResponse[]>(this.API_URL);
  }

  // Filtrar por planilla (Útil para mostrar solo opciones de Docentes o solo Admin)
  listarPorPlanilla(planilla: TipoPlanilla): Observable<TipoActividadResponse[]> {
    return this.http.get<TipoActividadResponse[]>(`${this.API_URL}/planilla/${planilla}`);
  }

  registrar(tipo: TipoActividadRequest): Observable<TipoActividadResponse> {
    return this.http.post<TipoActividadResponse>(this.API_URL, tipo);
  }

  buscarPorId(id: number): Observable<TipoActividadResponse> {
    return this.http.get<TipoActividadResponse>(`${this.API_URL}/${id}`);
  }

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/${id}`);
  }
}