import { AdelantoResponse } from "./adelanto-response";
import { BonificacionResponse } from "./bonificacion-response";
import { DescuentoResponse } from "./descuento-response";

export interface PagoResponse {
  pagoId: number;
  fecha: string;
  contratoId: number;
  tipoPago: string;
  montoBase: number;
  montoActividad: number;
  netoPagar: number;
  totalBonificaciones: number;
  totalDeducciones: number;
  totalAdelantos: number;
  adelantos: AdelantoResponse[];
  bonificaciones: BonificacionResponse[];
  deducciones: DescuentoResponse[];
}