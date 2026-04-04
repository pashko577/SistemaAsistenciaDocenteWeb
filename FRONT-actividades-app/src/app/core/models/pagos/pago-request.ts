import { BonificacionRequest } from "./bonificacion-request";
import { DescuentoRequest } from "./descuento-request";

export interface PagoRequest {
  fecha: string; // Formato "yyyy-MM-dd"
  contratoId: number;
  adelantoIds?: number[];
  bonificaciones?: BonificacionRequest[];
  deducciones?: DescuentoRequest[];
}