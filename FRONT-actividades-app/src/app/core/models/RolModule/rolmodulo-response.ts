import { ModuloResponse } from "./modulo-response";

export interface RolModuloResponse {
  id: number;
  rolId: number;
  rolNombre: string;
  modulo: ModuloResponse; // Cambiamos campos planos por el objeto completo
}