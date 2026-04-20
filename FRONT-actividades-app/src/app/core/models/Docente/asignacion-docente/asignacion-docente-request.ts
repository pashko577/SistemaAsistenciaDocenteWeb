import { Estado } from "../../enums/estado";

export interface AsignacionDocenteRequest {
    estado: Estado;
    observaciones: string;
    docenteId: number;
    claseId: number;
    tipoActividadId: number;
}