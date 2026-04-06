import { EstadoAdelanto } from "../enums/estado-adelanto";

export interface AdelantoResponse {
  id: number;
  nombre: string;
  monto: number;
  estado: EstadoAdelanto;
  fechaCreacion: string;
  usuarioId: number;
  nombreCompletoPersonal: string;
  dniPersonal: string;
  pagoId?: number;
  fechaPago?: string;
}