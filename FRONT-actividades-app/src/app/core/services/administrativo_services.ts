// services/administrativo.service.ts
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AdministrativoResponse } from '../models/Administrativos/administrativo-response';
import { AdministrativoRequest } from '../models/Administrativos/administrativo-request';
import { CargoAdministrativoResponse } from '../models/Administrativos/cargo-administrativo-response';
import { TipoDocumentoResponse } from '../models/tipo-documento-response';
import { SedeResponse } from '../models/sede-response';

@Injectable({ providedIn: 'root' })
export class AdministrativoService {
  private readonly API_URL = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Listar administrativos para el grid
  listar(): Observable<AdministrativoResponse[]> {
    return this.http.get<AdministrativoResponse[]>(`${this.API_URL}/administrativos`);
  }

  listarConContrato(): Observable<AdministrativoResponse[]> {
    return this.http.get<AdministrativoResponse[]>(`${this.API_URL}/administrativos/con-contrato`);
  }
  // Registrar nuevo
  registrar(admin: AdministrativoRequest): Observable<any> {
    return this.http.post(`${this.API_URL}/administrativos`, admin);
  }

// En administrativo.service.ts
actualizar(id: number, admin: any): Observable<any> {
  return this.http.put(`${this.API_URL}/administrativos/${id}`, admin);
}

  eliminar(id: number): Observable<any> {
    return this.http.delete(`${this.API_URL}/administrativos/${id}`);
  }

  // Métodos para cargar los Selects del Formulario
  listarCargos(): Observable<CargoAdministrativoResponse[]> {
    return this.http.get<CargoAdministrativoResponse[]>(`${this.API_URL}/cargo-administrativos`);
  }

  listarTiposDocumento(): Observable<TipoDocumentoResponse[]> {
    return this.http.get<TipoDocumentoResponse[]>(`${this.API_URL}/tipo-documento`);
  }

  listarSedes(): Observable<SedeResponse[]> {
    return this.http.get<SedeResponse[]>(`${this.API_URL}/sedes`);
  }
}