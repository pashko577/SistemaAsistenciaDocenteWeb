import { Estado } from "./enums/estado";

export interface DocenteRequest {
    // Usuario
    dni: string;
    password: string;
    sedeId: number;

    // Persona
    nombres: string;
    apellidos: string;
    celular: string;
    email: string;
    direccion: string;
    tipoDocumentoId: number;

    // Docente
    especialidadId: number;
    estado?: Estado; // Opcional, ya que el back lo pone por defecto
    observaciones: string;
}

