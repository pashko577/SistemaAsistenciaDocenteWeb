import { Estado } from "./enums/estado";

export interface AdministrativoRequest {
  dni: string;
  password?: string;
  sedeId: number;
  nombres: string;
  apellidos: string;
  celular: number;
  email: string;
  direccion: string;
  tipoDocumentoId: number;
  cargoAdministrativoId: number;
  estado: Estado;
}