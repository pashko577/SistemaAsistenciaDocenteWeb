import { EstadoAdelanto } from "../enums/estado-adelanto";

export interface AdelantoRequest {
  nombre: string;
  monto: number;
  usuarioId: number;
  fechaCreacion: string; // Enviamos como string YYYY-MM-DD
  estado?: EstadoAdelanto;
}