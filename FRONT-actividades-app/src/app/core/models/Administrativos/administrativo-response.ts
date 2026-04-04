import { Estado } from "../enums/estado";

export interface AdministrativoResponse {
  id: number;
  usuarioId: number;
  contratoId: number;
  // PERSONA
  dni: string;
  nombres: string;
  apellidos: string;
  celular: number;
  email: string;
  direccion: string;

  // RELACIONES
  sedeId: number;
  nombreSede: string;

  cargoAdministrativoId: number;
  nombreCargo: string;

  // ESTADO
  estado: Estado;
}