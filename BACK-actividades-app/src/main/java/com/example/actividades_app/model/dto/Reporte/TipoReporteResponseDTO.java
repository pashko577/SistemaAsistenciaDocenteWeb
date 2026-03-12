package com.example.actividades_app.model.dto.Reporte;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoReporteResponseDTO {

    private Long id;

    private String nombreTipoReporte;

}