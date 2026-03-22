export interface AsistenciaViewModel {
  // Datos del Administrativo (Vienen del Cronograma)
  administrativoId: number;
  nombreCompleto: string;
  cronogramaId: number;
  horaEntradaProg: string; // Ej: 08:00
  horaSalidaProg: string;   // Ej: 17:00

  // Datos para transcribir (Vienen de la Asistencia)
  asistenciaId?: number;    // Si ya existe en BD
  horaIngreso?: string;
  salidaAlmuerzo?: string;
  retornoAlmuerzo?: string;
  horaSalida?: string;
  terno: boolean;
  observaciones: string;
  
  // Estado de la UI
  cargando: boolean;
  modificado: boolean;
}