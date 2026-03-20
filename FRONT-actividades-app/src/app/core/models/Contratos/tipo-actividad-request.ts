import { TipoPlanilla } from "../enums/contrato-enums";

export interface TipoActividadRequest {
  nombre: string;
  tipoPlanilla: TipoPlanilla;
}