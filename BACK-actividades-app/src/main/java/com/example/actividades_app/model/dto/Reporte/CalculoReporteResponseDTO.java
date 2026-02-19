package com.example.actividades_app.model.dto.Reporte;


import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Builder
public class CalculoReporteResponseDTO {

    private BigDecimal pagoBase;
    private BigDecimal descuentoTardanza;
    private BigDecimal descuentoFaltas;
    private BigDecimal descuentoCriterios;
    private BigDecimal totalFinal;
}
