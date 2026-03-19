import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DocenteRequest } from '../models/docente-request';
import { DocenteResponse } from '../models/docente-response';

@Injectable({
  providedIn: 'root',
})
export class DocenteService {
  // Asegúrate de que la URL coincida con tu @RequestMapping del Controller
  private API_URL = 'http://localhost:8080/api/Docentes';

  constructor(private http: HttpClient) {}

  listarDocentes(): Observable<DocenteResponse[]> {
    return this.http.get<DocenteResponse[]>(this.API_URL);
  }

  registrarDocente(docente: DocenteRequest): Observable<DocenteResponse> {
    return this.http.post<DocenteResponse>(this.API_URL, docente);
  }

  actualizarDocente(id: number, docente: DocenteRequest): Observable<DocenteResponse> {
    return this.http.put<DocenteResponse>(`${this.API_URL}/${id}`, docente);
  }

  eliminarDocente(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }

  // Métodos adicionales para los combos (Usados en el forkJoin del componente)
  listarSedes(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/sedes');
  }

  listarEspecialidades(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/especialidades');
  }

  listarTiposDocumento(): Observable<any[]> {
    return this.http.get<any[]>('http://localhost:8080/api/tipos-documento');
  }
}