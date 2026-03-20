import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ContratoResponse } from '../models/Contratos/contrato-response';
import { ContratoRequest } from '../models/Contratos/contrato-request'; 

@Injectable({
  providedIn: 'root'
})
export class ContratoService {
  // Asegúrate de que el puerto y el prefijo /api coincidan con tu backend
  private readonly API_URL = 'http://localhost:8080/api/contratos';

  constructor(private http: HttpClient) {}

  // MÉTODOS DE LECTURA: Todos devuelven ContratoResponse para que el 'id' no sea undefined
  listar(): Observable<ContratoResponse[]> {
    return this.http.get<ContratoResponse[]>(this.API_URL);
  }

  buscarPorId(id: number): Observable<ContratoResponse> {
    return this.http.get<ContratoResponse>(`${this.API_URL}/${id}`);
  }

  buscarPorUsuario(usuarioId: number): Observable<ContratoResponse> {
    return this.http.get<ContratoResponse>(`${this.API_URL}/usuario/${usuarioId}`);
  }

  // MÉTODOS DE ESCRITURA: Reciben el Request y devuelven el Response creado/editado
  registrar(contrato: ContratoRequest): Observable<ContratoResponse> {
    return this.http.post<ContratoResponse>(this.API_URL, contrato);
  }

  actualizar(id: number, contrato: ContratoRequest): Observable<ContratoResponse> {
    return this.http.put<ContratoResponse>(`${this.API_URL}/${id}`, contrato);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}