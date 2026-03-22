import { TipoAsistencia } from "../enums/tipo-asistencia-enum";

export interface AsistenciaAdministrativoResponse {
    id: number;
    horaIngreso: string;
    horaSalida: string;
    salidaAlmuerzo: string;
    retornoAlmuerzo: string;
    fecha: string;
    observaciones: string;
    tardanza: number;         // Los minutos calculados por tu Backend
    terno: boolean;
    tipoAsistencia: TipoAsistencia;
    administrativoId: number;
    cronogramaAdministrativoId: number;
    // Agregamos este campo extra para la UI
    nombreAdministrativo?: string; 
}