import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { TipoDocumentoRequest } from '../models/tipo-documento-request';
import { Observable } from 'rxjs';
import { TipoDocumentoResponse } from '../models/tipo-documento-response';

@Injectable({
  providedIn: 'root',
})
export class TipoDocumentoService {
  // Usando la ruta exacta de tu Controller
  private API_URL = 'http://localhost:8080/api/TipoDocumento';

  constructor(private http: HttpClient) {}

  // POST: Crear Tipo Documento
  crearTipoDocumento(dto: TipoDocumentoRequest): Observable<TipoDocumentoResponse> {
    return this.http.post<TipoDocumentoResponse>(this.API_URL, dto);
  }

  // GET: Listar todos
  listar(): Observable<TipoDocumentoResponse[]> {
    return this.http.get<TipoDocumentoResponse[]>(this.API_URL);
  }

  // GET: Obtener por ID
  obtenerPorId(id: number): Observable<TipoDocumentoResponse> {
    return this.http.get<TipoDocumentoResponse>(`${this.API_URL}/${id}`);
  }

  // DELETE: Eliminar
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
