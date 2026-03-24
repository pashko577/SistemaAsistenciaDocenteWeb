import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PlanillaAdministrativoDTO } from '../models/Administrativos/planillaAdministrativoDTO';

@Injectable({
  providedIn: 'root'
})
export class PlanillaAdministrativoService {
  
  // URL base unificada
  private readonly API_URL = 'http://localhost:8080/api/planilla-administrativo';

  constructor(private http: HttpClient) { }

  /**
   * Consulta el endpoint: GET /api/planilla-administrativo/{id}?inicio=YYYY-MM-DD&fin=YYYY-MM-DD
   */
  calcularPlanilla(id: number, inicio: string, fin: string): Observable<PlanillaAdministrativoDTO> {
    const params = new HttpParams()
      .set('inicio', inicio)
      .set('fin', fin);

    return this.http.get<PlanillaAdministrativoDTO>(`${this.API_URL}/${id}`, { params });
  }
}