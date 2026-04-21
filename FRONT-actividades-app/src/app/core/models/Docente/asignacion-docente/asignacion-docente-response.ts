import { Estado } from "../../enums/estado";

export interface AsignacionDocenteResponse {
    id: number;
    estado: Estado;
    observaciones: string;
    docenteId: number;
    docenteNombre: string;
    claseId: number;

    cursoNombre: string;
    gradoNombre: string;
    seccionNombre: string;
    nivelNombre: string;
    // Estos campos ayudan a que la UI se vea como tus capturas previas
    tipoActividadId: number;
    tipoActividadNombre: string;
}