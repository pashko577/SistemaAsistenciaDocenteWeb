import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { NivelResponse } from '../../models/Docente/clase/nivel-response';
import { NivelRequest } from '../../models/Docente/clase/nivel-request';

@Injectable({
    providedIn: 'root'
})
export class NivelService {
    private readonly API_URL = 'http://localhost:8080/api/niveles';

    constructor(private http: HttpClient) { }

    listar(): Observable<NivelResponse[]> {
        return this.http.get<NivelResponse[]>(this.API_URL);
    }

    obtenerPorId(id: number): Observable<NivelResponse> {
        return this.http.get<NivelResponse>(`${this.API_URL}/${id}`);
    }

    crear(request: NivelRequest): Observable<NivelResponse> {
        return this.http.post<NivelResponse>(this.API_URL, request);
    }

    actualizar(id: number, request: NivelRequest): Observable<NivelResponse> {
        return this.http.put<NivelResponse>(`${this.API_URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}