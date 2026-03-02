import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Token } from './token';
import { Router } from '@angular/router';
import { DocenteRequest } from '../models/docente-request';
import { Observable } from 'rxjs';
import { DocenteResponse } from '../models/docente-response';

@Injectable({
  providedIn: 'root',
})
export class Docente {
  private API_URL = 'http://localhost:8080/api/Docentes';

  constructor(
    private http: HttpClient,
  ){}

  registrarDocente(docente: DocenteRequest): Observable<void>{
    return this.http.post<void>(this.API_URL, docente);
  }

  listarDocentes(): Observable<DocenteResponse[]> {
    return this.http.get<DocenteResponse[]>(this.API_URL);
  }
}
