import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PagoRequest } from '../models/pagos/pago-request';
import { PagoResponse } from '../models/pagos/pago-response';


@Injectable({
  providedIn: 'root'
})
export class PagosService {
  private readonly URL = 'http://localhost:8080/api/pagos';

  constructor(private http: HttpClient) {}

  // =========================
  // MÉTODOS CORE (CRUD)
  // =========================

  crear(pago: PagoRequest): Observable<PagoResponse> {
    return this.http.post<PagoResponse>(this.URL, pago);
  }

  actualizar(id: number, pago: PagoRequest): Observable<PagoResponse> {
    return this.http.put<PagoResponse>(`${this.URL}/${id}`, pago);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.URL}/${id}`);
  }

  buscarPorId(id: number): Observable<PagoResponse> {
    return this.http.get<PagoResponse>(`${this.URL}/${id}`);
  }

  // =========================
  // BÚSQUEDAS FILTRADAS
  // =========================

  listarPorUsuario(usuarioId: number): Observable<PagoResponse[]> {
    return this.http.get<PagoResponse[]>(`${this.URL}/usuario/${usuarioId}`);
  }


  listarPorFecha(fecha: string): Observable<PagoResponse[]> {
    const params = new HttpParams().set('fecha', fecha);
    return this.http.get<PagoResponse[]>(`${this.URL}/fecha`, { params });
  }

  listarPorRango(inicio: string, fin: string): Observable<PagoResponse[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<PagoResponse[]>(`${this.URL}/rango`, { params });
  }

  // Útil para la vista de "Reporte de Asistencia" que mostraste
  listarPorUsuarioYRango(usuarioId: number, inicio: string, fin: string): Observable<PagoResponse[]> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);
    return this.http.get<PagoResponse[]>(`${this.URL}/usuario/${usuarioId}/rango`, { params });
  }

  obtenerUltimoPago(usuarioId: number): Observable<PagoResponse> {
    return this.http.get<PagoResponse>(`${this.URL}/usuario/${usuarioId}/ultimo`);
  }

  // Para generar la Boleta Final
  obtenerResumenBoleta(pagoId: number): Observable<any> {
    return this.http.get<any>(`${this.URL}/${pagoId}/resumen`);
  }
}