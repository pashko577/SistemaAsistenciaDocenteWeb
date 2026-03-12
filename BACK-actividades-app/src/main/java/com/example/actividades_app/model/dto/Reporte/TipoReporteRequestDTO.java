package com.example.actividades_app.model.dto.Reporte;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoReporteRequestDTO {

    @NotBlank(message = "El nombre del tipo de reporte es obligatorio")
    private String nombreTipoReporte;

}