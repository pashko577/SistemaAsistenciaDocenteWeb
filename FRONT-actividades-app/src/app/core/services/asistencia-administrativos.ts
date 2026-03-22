import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AsistenciaAdministrativoRequest } from '../models/Administrativos/asistenciaAdministrativos-Request';
import { AsistenciaAdministrativoResponse } from '../models/Administrativos/asistenciaAdminsitrativos-Response';


@Injectable({
  providedIn: 'root'
})
export class AsistenciaAdministrativoService {
  
  private readonly URL_API = 'http://localhost:8080/api/asistencia-administrativo'; // Ajusta tu puerto

  constructor(private http: HttpClient) { }

  // Para que el personal de control registre una fila nueva
  registrar(dto: AsistenciaAdministrativoRequest): Observable<AsistenciaAdministrativoResponse> {
    return this.http.post<AsistenciaAdministrativoResponse>(this.URL_API, dto);
  }

  // Para actualizar los campos (Salida, Almuerzo, etc.)
  actualizar(id: number, dto: AsistenciaAdministrativoRequest): Observable<AsistenciaAdministrativoResponse> {
    return this.http.put<AsistenciaAdministrativoResponse>(`${this.URL_API}/${id}`, dto);
  }

// En tu archivo de servicio:
listarAsistenciasPorFecha(fecha: string): Observable<AsistenciaAdministrativoResponse[]> {
  // Asegúrate de que el nombre sea este ----------------^
  return this.http.get<AsistenciaAdministrativoResponse[]>(`${this.URL_API}/fecha/${fecha}`);
}
}