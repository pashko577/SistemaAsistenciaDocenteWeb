import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AsistenciaAdministrativoRequest } from '../models/Administrativos/asistenciaAdministrativos-Request';
import { AsistenciaAdministrativoResponse } from '../models/Administrativos/asistenciaAdminsitrativos-Response';

@Injectable({
  providedIn: 'root'
})
export class AsistenciaAdministrativoService {
  
  // Usamos una sola constante para evitar errores de referencia
  private readonly API_URL = 'http://localhost:8080/api/asistencia-administrativo';

  constructor(private http: HttpClient) { }

  /**
   * REGISTRAR: POST /api/asistencia-administrativo
   */
  registrar(dto: AsistenciaAdministrativoRequest): Observable<AsistenciaAdministrativoResponse> {
    return this.http.post<AsistenciaAdministrativoResponse>(this.API_URL, dto);
  }

  /**
   * ACTUALIZAR: PUT /api/asistencia-administrativo/{id}
   */
  actualizar(id: number, dto: AsistenciaAdministrativoRequest): Observable<AsistenciaAdministrativoResponse> {
    return this.http.put<AsistenciaAdministrativoResponse>(`${this.API_URL}/${id}`, dto);
  }

  /**
   * LISTAR POR FECHA (Para el Módulo de Asistencia diaria)
   * GET /api/asistencia-administrativo/fecha/{fecha}
   */
  listarPorFecha(fecha: string): Observable<AsistenciaAdministrativoResponse[]> {
    return this.http.get<AsistenciaAdministrativoResponse[]>(`${this.API_URL}/fecha/${fecha}`);
  }

  /**
   * LISTAR POR PERIODO (Para el Reporte Mensual)
   * GET /api/asistencia-administrativo/periodo/{administrativoId}?inicio=...&fin=...
   */
  listarPorPeriodo(id: number, inicio: string, fin: string): Observable<AsistenciaAdministrativoResponse[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    
    return this.http.get<AsistenciaAdministrativoResponse[]>(`${this.API_URL}/periodo/${id}`, { params });
  }

  /**
   * ELIMINAR: DELETE /api/asistencia-administrativo/{id}
   */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}