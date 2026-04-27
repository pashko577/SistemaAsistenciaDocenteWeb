import { DiaSemana } from "../../enums/dia-semana";


export interface CronogramaDocenteResponse {
    id: number;
    asignacionDocenteId: number;
    horarioBloqueId: number;
    horaInicio: string;
    horaFin: string;
    diaSemana: DiaSemana;

    // Campos descriptivos
    docenteNombre: string;
    cursoNombre: string;
    gradoSeccion: string;
    nivelNombre: string;
}