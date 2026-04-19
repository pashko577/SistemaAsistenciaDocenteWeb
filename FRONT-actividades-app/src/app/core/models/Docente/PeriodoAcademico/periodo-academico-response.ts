import { EstadoPeriodo } from "../../enums/estado-periodo";

export interface PeriodoAcademicoResponse {
    id: number;
    nombre: string;
    fechaInicio: string; // El LocalDate de Java llega como ISO String (YYYY-MM-DD)
    fechaFin: string;
    estado: EstadoPeriodo;
}