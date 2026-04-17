import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CursoResponse } from '../../models/Docente/clase/curso-response';
import { CursoRequest } from '../../models/Docente/clase/curso-request';


@Injectable({
    providedIn: 'root'
})
export class CursoService {
    private readonly API_URL = 'http://localhost:8080/api/cursos';

    constructor(private http: HttpClient) { }

    listar(): Observable<CursoResponse[]> {
        return this.http.get<CursoResponse[]>(this.API_URL);
    }

    obtenerPorId(id: number): Observable<CursoResponse> {
        return this.http.get<CursoResponse>(`${this.API_URL}/${id}`);
    }

    crear(request: CursoRequest): Observable<CursoResponse> {
        return this.http.post<CursoResponse>(this.API_URL, request);
    }

    actualizar(id: number, request: CursoRequest): Observable<CursoResponse> {
        return this.http.put<CursoResponse>(`${this.API_URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}