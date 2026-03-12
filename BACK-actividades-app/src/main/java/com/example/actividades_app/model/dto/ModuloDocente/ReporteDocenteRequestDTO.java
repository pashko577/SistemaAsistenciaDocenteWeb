package com.example.actividades_app.model.dto.ModuloDocente;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteDocenteRequestDTO {

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    private String observaciones;

    private Integer tardanza;

    @NotNull(message = "El cronograma diario es obligatorio")
    private Long cronogramaDiarioId;

    @NotNull(message = "El tipo de reporte es obligatorio")
    private Long tipoReporteId;

}