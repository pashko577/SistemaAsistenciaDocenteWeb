package com.example.actividades_app.model.dto.ModuloDocente;

import java.math.BigDecimal;

import com.example.actividades_app.model.Entity.Contrato.TipoPago;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanillaDocenteDTO {

    private Long docenteId;

    private TipoPago tipoPago;

    private BigDecimal sueldoBase;

    // Actividad académica
    private Integer horasDictadas;

    // Asistencia
    private Integer faltas;
    private Integer permisos;
    private Integer tardanzas;
    private Integer minutosTardanza;

    // Criterios del reporte académico
    private Integer criterio1SI;
    private Integer criterio1REG;
    private Integer criterio1NO;

    private Integer criterio2SI;
    private Integer criterio2REG;
    private Integer criterio2NO;

    // Descuentos
    private BigDecimal descuentoFaltas;
    private BigDecimal descuentoTardanza;
    private BigDecimal descuentoCriterios;

    // Totales
    private BigDecimal sueldoBruto;
    private BigDecimal sueldoNeto;
}