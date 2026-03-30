package com.example.actividades_app.model.dto.ModuloDocente;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReporteDocenteResponseDTO {
    private Long id;
    private LocalDate fecha; 
    private String observaciones;
    private Integer tardanza; // <--- Asegúrate de que esta línea esté aquí
    private Long cronogramaDiarioId;
    private Long tipoReporteId;
    private String tipoReporteNombre;
}