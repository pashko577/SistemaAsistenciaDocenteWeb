package com.example.actividades_app.model.dto.Reporte;


import lombok.Data;
import java.math.BigDecimal;

@Data
public class CalculoReporteRequestDTO {

    // =============================
    // INFORMACIÓN DE PAGO
    // =============================
    private String tipoPago; // "POR_HORA" o "MENSUAL"

    private BigDecimal tarifaPorHora;
    private BigDecimal sueldoMensual;
    private Integer horasMensualesReferencia; // necesario si es mensual

    // =============================
    // HORAS TRABAJADAS
    // =============================
    private Integer horasDictadas;

    // =============================
    // TARDANZAS
    // =============================
    private Integer sumaMinutosTardanza;
    private Integer cantidadDiasConClase;

    // =============================
    // FALTAS
    // =============================
    private Integer cantidadFaltas;

    // =============================
    // CRITERIOS (INCUMPLIMIENTOS)
    // =============================
    private Integer incumplimientosWhatsapp;  // criterio 1
    private Integer incumplimientosFicha;     // criterio 2
    private Integer incumplimientosAgenda;    // criterio 3
}
