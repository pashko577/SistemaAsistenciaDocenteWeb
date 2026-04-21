import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

// Modelos principales del módulo
import { AsignacionDocenteResponse } from '../../models/Docente/asignacion-docente/asignacion-docente-response';
import { AsignacionDocenteRequest } from '../../models/Docente/asignacion-docente/asignacion-docente-request';
import { DocenteResponse } from '../../models/docente-response';
import { ClaseResponse } from '../../models/Docente/clase/clase-response';
import { TipoActividadResponse } from '../../models/Contratos/tipo-actividad-response';

// Modelos auxiliares para los selects (Asegúrate de que estas rutas existan)


@Injectable({ providedIn: 'root' })
export class AsignacionDocenteService {
    private readonly API_URL = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    // ==========================================
    // OPERACIONES CRUD (Asignación)
    // ==========================================

    listar(): Observable<AsignacionDocenteResponse[]> {
        return this.http.get<AsignacionDocenteResponse[]>(`${this.API_URL}/asignacion-docente`);
    }

    obtenerPorId(id: number): Observable<AsignacionDocenteResponse> {
        return this.http.get<AsignacionDocenteResponse>(`${this.API_URL}/asignacion-docente/${id}`);
    }

    registrar(data: AsignacionDocenteRequest): Observable<AsignacionDocenteResponse> {
        return this.http.post<AsignacionDocenteResponse>(`${this.API_URL}/asignacion-docente`, data);
    }

    actualizar(id: number, data: AsignacionDocenteRequest): Observable<AsignacionDocenteResponse> {
        return this.http.put<AsignacionDocenteResponse>(`${this.API_URL}/asignacion-docente/${id}`, data);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.API_URL}/asignacion-docente/${id}`);
    }

    // ==========================================
    // MÉTODOS PARA SELECTORES (FORMULARIO)
    // ==========================================

    /** * Obtiene la lista de docentes disponibles. 
     * Nota: Usa 'Docentes' con D mayúscula según tu controlador.
     */
    listarDocentes(): Observable<DocenteResponse[]> {
        return this.http.get<DocenteResponse[]>(`${this.API_URL}/Docentes/con-contrato`);
    }

    /** * Obtiene las clases académicas configuradas.
     */
    listarClases(): Observable<ClaseResponse[]> {
        return this.http.get<ClaseResponse[]>(`${this.API_URL}/clases`);
    }

    /** * Obtiene los tipos de actividad (Pedagógica, etc).
     */
    listarTiposActividad(): Observable<TipoActividadResponse[]> {
        return this.http.get<TipoActividadResponse[]>(`${this.API_URL}/tipo-actividad`);
    }
}