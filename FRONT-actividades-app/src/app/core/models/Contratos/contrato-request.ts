import { TipoPago } from "../enums/contrato-enums";
import { Estado } from "../enums/estado";

export interface ContratoRequest {
  tipoPago: TipoPago;
  montoBase: number;
  horasJornada: number;
  diasLaborablesMes: number;
  usuarioId: number;
  tipoActividadId: number;
  estado: Estado;
}