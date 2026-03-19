export interface DocenteResponse {
  id: number;
  dni: string;
  nombres: string;
  apellidos: string;
  email: string;
  celular: string;
  direccion: string;
  sedeId: number;           
  especialidadId: number;
  nombreSede: string;
  nombreEspecialidad: string;
  tipoDocumentoNombre: string;
  estado: string;
  observaciones: string;
}
