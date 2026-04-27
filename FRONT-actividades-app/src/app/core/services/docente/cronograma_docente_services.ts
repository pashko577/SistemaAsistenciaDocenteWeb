import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CronogramaDocenteRequest } from '../../models/Docente/cronograma/cronograma-docente-request';
import { CronogramaDocenteResponse } from '../../models/Docente/cronograma/cronograma-docente-response';
import { AsignacionDocenteResponse } from '../../models/Docente/asignacion-docente/asignacion-docente-response'; 
import { HorarioBloqueResponse } from '../../models/Docente/horario-bloque/horario-bloque-response';

@Injectable({
    providedIn: 'root'
})
export class CronogramaDocenteService {
    private readonly URL_BASE = 'http://localhost:8080/api';

    constructor(private http: HttpClient) { }

    // 1. Guardar el cronograma maestro
    crearCronograma(request: CronogramaDocenteRequest): Observable<CronogramaDocenteResponse> {
        // Sincronizado con el backend: cronogramas (plural)
        return this.http.post<CronogramaDocenteResponse>(`${this.URL_BASE}/cronogramas-docente`, request);
    }

    // 2. Obtener bloques horarios
    listarBloquesHorarios(): Observable<HorarioBloqueResponse[]> {
        return this.http.get<HorarioBloqueResponse[]>(`${this.URL_BASE}/horarios-bloque`);
    }

    // 3. Obtener asignaciones reales (Docente-Clase)
    listarAsignacionesActivas(): Observable<AsignacionDocenteResponse[]> {
        return this.http.get<AsignacionDocenteResponse[]>(`${this.URL_BASE}/asignacion-docente`);
    }

    // 4. Listar cronogramas (plural)
    listarCronogramas(): Observable<CronogramaDocenteResponse[]> {
        return this.http.get<CronogramaDocenteResponse[]>(`${this.URL_BASE}/cronogramas-docente`);
    }

    eliminar(id: number): Observable<void> {
        return this.http.delete<void>(`${this.URL_BASE}/cronogramas-docente/${id}`);
    }
}