import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PeriodoAcademicoResponse } from '../../models/Docente/PeriodoAcademico/periodo-academico-response';
import { PeriodoAcademicoRequest } from '../../models/Docente/PeriodoAcademico/periodo-academico-request';


@Injectable({
    providedIn: 'root'
})
export class PeriodoAcademicoService {
    private readonly http = inject(HttpClient);
    private readonly API_URL = 'http://localhost:8080/api/periodos';

    /**
     * Obtiene todos los periodos académicos
     * Acceso: ADMIN, DOCENTE, ADMINISTRATIVO (@IsStaff)
     */
    listar(): Observable<PeriodoAcademicoResponse[]> {
        return this.http.get<PeriodoAcademicoResponse[]>(this.API_URL);
    }

    /**
     * Obtiene un periodo por su ID
     */
    obtenerPorId(id: number): Observable<PeriodoAcademicoResponse> {
        return this.http.get<PeriodoAcademicoResponse>(`${this.API_URL}/${id}`);
    }

    /**
     * Crea un nuevo periodo académico
     * Acceso: Solo ADMIN
     */
    crear(request: PeriodoAcademicoRequest): Observable<PeriodoAcademicoResponse> {
        return this.http.post<PeriodoAcademicoResponse>(this.API_URL, request);
    }

    /**
     * Actualiza un periodo existente
     * Acceso: Solo ADMIN
     */
    actualizar(id: number, request: PeriodoAcademicoRequest): Observable<PeriodoAcademicoResponse> {
        return this.http.put<PeriodoAcademicoResponse>(`${this.API_URL}/${id}`, request);
    }

    /**
     * Elimina un periodo académico
     * Acceso: Solo ADMIN
     */
    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/${id}`);
    }
}