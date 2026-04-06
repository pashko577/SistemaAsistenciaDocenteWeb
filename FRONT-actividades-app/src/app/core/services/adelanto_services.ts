import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdelantoRequest } from '../models/pagos/adelanto-request';
import { AdelantoResponse } from '../models/pagos/adelanto-response';


@Injectable({
  providedIn: 'root'
})
export class AdelantoService {
  private readonly URL = 'http://localhost:8080/api/adelantos';

  constructor(private http: HttpClient) {}

  // Registrar un adelanto nuevo (ej. a mitad de mes)
  registrar(request: AdelantoRequest): Observable<AdelantoResponse> {
    return this.http.post<AdelantoResponse>(`${this.URL}/registrar`, request);
  }
// Actualizar un adelanto existente (Monto o Concepto)
actualizar(id: number, request: AdelantoRequest): Observable<AdelantoResponse> {
  return this.http.put<AdelantoResponse>(`${this.URL}/actualizar/${id}`, request);
}
  // Para el historial general de adelantos
  listarTodos(): Observable<AdelantoResponse[]> {
    return this.http.get<AdelantoResponse[]>(`${this.URL}/todos`);
  }

  // FUNDAMENTAL: Para listar los adelantos que aparecerán en la boleta
  listarPendientesPorUsuario(usuarioId: number): Observable<AdelantoResponse[]> {
    return this.http.get<AdelantoResponse[]>(`${this.URL}/pendientes/usuario/${usuarioId}`);
  }

  // Para mostrar el "Monto a Descontar" en el resumen visual
  obtenerTotalPendiente(usuarioId: number): Observable<number> {
    return this.http.get<number>(`${this.URL}/total-pendiente/usuario/${usuarioId}`);
  }

  // Por si el administrador se equivoca y debe cancelar un adelanto
  anular(id: number): Observable<void> {
    return this.http.put<void>(`${this.URL}/anular/${id}`, {});
  }
}