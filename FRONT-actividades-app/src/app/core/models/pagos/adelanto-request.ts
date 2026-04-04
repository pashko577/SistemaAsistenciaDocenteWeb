import { EstadoAdelanto } from "../enums/estado-adelanto";

export interface AdelantoRequest {
  nombre: string;
  monto: number;
  usuarioId: number;
  estado?: EstadoAdelanto;
}