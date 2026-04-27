import { DiaSemana } from "../../enums/dia-semana";


export interface CronogramaDocenteRequest {
    asignacionDocenteId: number;
    horarioBloqueId: number;
    diaSemana: DiaSemana;
}