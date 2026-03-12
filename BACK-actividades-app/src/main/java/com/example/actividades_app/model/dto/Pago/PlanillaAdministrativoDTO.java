package com.example.actividades_app.model.dto.Pago;

import java.math.BigDecimal;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlanillaAdministrativoDTO {

    private Long administrativoId;

    private BigDecimal sueldoBase;

    private Integer faltas;

    private Integer minutosTardanza;

    private BigDecimal descuentoFaltas;

    private BigDecimal descuentoTardanza;

    private BigDecimal sueldoNeto;
}