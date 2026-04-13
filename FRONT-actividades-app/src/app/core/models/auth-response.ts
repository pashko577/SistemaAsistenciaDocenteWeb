import { ModuloResponse } from './RolModule/modulo-response';
export interface AuthResponse {

    token : string;
    dni: string;
    roles: string[];
    message: string;
    rutas_permitidas: ModuloResponse
}
