package com.example.actividades_app.model.dto.Pago;

import java.math.BigDecimal;

import com.example.actividades_app.model.Entity.Pago.TipoPago;

import lombok.Data;

@Data
public class PagoCalculoRequestDTO {

    private Long usuarioId;

    private TipoPago tipoPago;

    // horas y tarifa
    private BigDecimal horasTrabajadas;
    private BigDecimal tarifaHora;

    // mensual
    private BigDecimal sueldoMensual;

    // tardanzas
    private Integer minutosTardanza;

    // faltas
    private Integer faltas;

    private BigDecimal factorDescuentoFalta; // ej 0.5

    // cumplimiento
    private Integer incumplimientosWhatsapp;
    private Integer incumplimientosFicha;
    private Integer incumplimientosAgenda;

    // bonificaciones
    private BigDecimal bonificaciones;
}