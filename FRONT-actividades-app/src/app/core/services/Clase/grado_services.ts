import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { GradoResponse } from '../../models/Docente/clase/grado-response';
import { GradoRequest } from '../../models/Docente/clase/grado-request';

@Injectable({
    providedIn: 'root'
})
export class GradoService {
    private readonly API_URL = 'http://localhost:8080/api/grados';

    constructor(private http: HttpClient) { }

    listar(): Observable<GradoResponse[]> {
        return this.http.get<GradoResponse[]>(this.API_URL);
    }

    obtenerPorId(id: number): Observable<GradoResponse> {
        return this.http.get<GradoResponse>(`${this.API_URL}/${id}`);
    }

    crear(request: GradoRequest): Observable<GradoResponse> {
        return this.http.post<GradoResponse>(this.API_URL, request);
    }

    actualizar(id: number, request: GradoRequest): Observable<GradoResponse> {
        return this.http.put<GradoResponse>(`${this.API_URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}