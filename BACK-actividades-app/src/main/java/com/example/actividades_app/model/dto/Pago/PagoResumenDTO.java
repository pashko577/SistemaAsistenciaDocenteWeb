package com.example.actividades_app.model.dto.Pago;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagoResumenDTO {

    private Long pagoId;
    private BigDecimal montoActividad;
    private BigDecimal totalBonificaciones;
    private BigDecimal totalDeducciones;
    private BigDecimal totalAdelantos;
    private BigDecimal netoPagar;
}