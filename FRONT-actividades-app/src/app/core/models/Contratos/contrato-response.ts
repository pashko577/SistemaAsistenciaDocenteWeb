import { TipoPago } from "../enums/contrato-enums";
import { Estado } from "../enums/estado";

export interface ContratoResponse {
  id: number;
  tipoPago: TipoPago;
  montoBase: number;
  horasJornada: number;
  diasLaborablesMes: number;
  usuarioId: number;
  tipoActividadId: number;
  estado: Estado;
  // Opcionales para mostrar en la tabla sin hacer más joins
  usuarioNombre: string; 
  usuarioDni: string;
  nombreActividad: string; // Cambiado de actividadNombre a nombreActividad
  tipoPlanilla: string;
}