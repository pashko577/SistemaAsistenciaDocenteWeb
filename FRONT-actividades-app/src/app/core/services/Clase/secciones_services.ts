import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SeccionResponse } from '../../models/Docente/clase/seccion-response';
import { SeccionRequest } from '../../models/Docente/clase/seccion-request';

@Injectable({
    providedIn: 'root'
})
export class SeccionService {
    private readonly API_URL = 'http://localhost:8080/api/secciones';

    constructor(private http: HttpClient) { }

    listar(): Observable<SeccionResponse[]> {
        return this.http.get<SeccionResponse[]>(this.API_URL);
    }

    obtenerPorId(id: number): Observable<SeccionResponse> {
        return this.http.get<SeccionResponse>(`${this.API_URL}/${id}`);
    }

    crear(request: SeccionRequest): Observable<SeccionResponse> {
        return this.http.post<SeccionResponse>(this.API_URL, request);
    }

    actualizar(id: number, request: SeccionRequest): Observable<SeccionResponse> {
        return this.http.put<SeccionResponse>(`${this.API_URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}