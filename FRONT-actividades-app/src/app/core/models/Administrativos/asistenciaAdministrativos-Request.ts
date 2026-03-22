import { TipoAsistencia } from "../enums/tipo-asistencia-enum";

export interface AsistenciaAdministrativoRequest {
    horaIngreso?: string;      // Usamos string para que el formato sea "HH:mm:ss"
    horaSalida?: string;
    salidaAlmuerzo?: string;
    retornoAlmuerzo?: string;
    fecha: string;             // Formato "YYYY-MM-DD"
    observaciones?: string;
    terno: boolean;
    tipoAsistencia?: TipoAsistencia;
    administrativoId: number;
    cronogramaAdministrativoId: number;
}