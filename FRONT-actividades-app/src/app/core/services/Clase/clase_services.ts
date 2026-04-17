import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ClaseResponse } from '../../models/Docente/clase/clase-response';
import { ClaseRequest } from '../../models/Docente/clase/clase-request';

@Injectable({
    providedIn: 'root'
})
export class ClaseService {
    private readonly API_URL = 'http://localhost:8080/api/clases';

    constructor(private http: HttpClient) { }

    listar(): Observable<ClaseResponse[]> {
        return this.http.get<ClaseResponse[]>(this.API_URL);
    }

    obtenerPorId(id: number): Observable<ClaseResponse> {
        return this.http.get<ClaseResponse>(`${this.API_URL}/${id}`);
    }

    crear(request: ClaseRequest): Observable<ClaseResponse> {
        return this.http.post<ClaseResponse>(this.API_URL, request);
    }

    actualizar(id: number, request: ClaseRequest): Observable<ClaseResponse> {
        return this.http.put<ClaseResponse>(`${this.API_URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}