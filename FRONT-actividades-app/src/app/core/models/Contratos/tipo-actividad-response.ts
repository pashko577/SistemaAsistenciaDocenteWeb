import { TipoPlanilla } from "../enums/contrato-enums";

export interface TipoActividadResponse {
  id: number;
  nombre: string;
  tipoPlanilla: TipoPlanilla;
}