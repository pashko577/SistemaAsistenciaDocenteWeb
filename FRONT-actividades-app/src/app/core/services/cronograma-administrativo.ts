import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CronogramaAdministrativoResponse } from '../models/Administrativos/cronogramaadministrativo-response';
import { CronogramaAdministrativoRequest } from '../models/Administrativos/cronogramaadministrativo-request';


@Injectable({
  providedIn: 'root'
})
export class CronogramaAdministrativoService {

  private readonly API_URL = 'http://localhost:8080/api/cronograma-administrativo';

  constructor(private http: HttpClient) { }

  listarPorAdministrativo(adminId: number): Observable<CronogramaAdministrativoResponse[]> {
    return this.http.get<CronogramaAdministrativoResponse[]>(`${this.API_URL}/administrativo/${adminId}`);
  }

  // Este es CLAVE para la transcripción:
  // Trae a todos los que trabajan hoy para armar la lista de la hoja
  listarPorDiaSemana(dia: string): Observable<CronogramaAdministrativoResponse[]> {
    return this.http.get<CronogramaAdministrativoResponse[]>(`${this.API_URL}/dia/${dia}`);
  }

  crear(dto: CronogramaAdministrativoRequest): Observable<CronogramaAdministrativoResponse> {
    return this.http.post<CronogramaAdministrativoResponse>(this.API_URL, dto);
  }

  actualizar(id: number, dto: CronogramaAdministrativoRequest): Observable<CronogramaAdministrativoResponse> {
    return this.http.put<CronogramaAdministrativoResponse>(`${this.API_URL}/${id}`, dto);
  }

  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}