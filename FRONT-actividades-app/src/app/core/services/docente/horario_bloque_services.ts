import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { HorarioBloqueResponse } from '../../models/Docente/horario-bloque/horario-bloque-response';
import { HorarioBloqueRequest } from '../../models/Docente/horario-bloque/horario-bloque-request';


@Injectable({
    providedIn: 'root'
})
export class HorarioBloqueService {
    private readonly URL = 'http://localhost:8080/api/horarios-bloque';

    constructor(private http: HttpClient) { }

    listar(): Observable<HorarioBloqueResponse[]> {
        return this.http.get<HorarioBloqueResponse[]>(this.URL);
    }

    listarPorNivel(nivelId: number): Observable<HorarioBloqueResponse[]> {
        return this.http.get<HorarioBloqueResponse[]>(`${this.URL}/nivel/${nivelId}`);
    }

    obtenerPorId(id: number): Observable<HorarioBloqueResponse> {
        return this.http.get<HorarioBloqueResponse>(`${this.URL}/${id}`);
    }

    crear(request: HorarioBloqueRequest): Observable<HorarioBloqueResponse> {
        return this.http.post<HorarioBloqueResponse>(this.URL, request);
    }

    actualizar(id: number, request: HorarioBloqueRequest): Observable<HorarioBloqueResponse> {
        return this.http.put<HorarioBloqueResponse>(`${this.URL}/${id}`, request);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.URL}/${id}`);
    }
}