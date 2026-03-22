import { DiaSemana } from "../enums/dia-semana";

export interface CronogramaAdministrativoResponse {
    id: number;
    horaEntrada: string;
    horaSalida: string;
    diaSemana: DiaSemana;
    administrativoId: number;
}