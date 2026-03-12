package com.example.actividades_app.model.dto.ModuloDocente;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DetalleReporteDocenteRequestDTO {

    @NotNull(message = "El reporte docente es obligatorio")
    private Long reporteDocenteId;

    private Boolean publiWSS;

    private Boolean publiWFS;

    private Boolean agendaE;

}