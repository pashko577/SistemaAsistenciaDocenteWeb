import { DiaSemana } from "../enums/dia-semana";

export interface CronogramaAdministrativoRequest {
    horaEntrada: string; // Formato "HH:mm:ss"
    horaSalida: string;
    diaSemana: DiaSemana;
    administrativoId: number;
}