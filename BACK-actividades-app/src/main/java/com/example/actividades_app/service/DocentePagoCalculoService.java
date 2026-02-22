package com.example.actividades_app.service;

import java.math.BigDecimal;

import com.example.actividades_app.model.Entity.Pago;
import com.example.actividades_app.model.dto.Pago.PagoCalculoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoCalculoResponseDTO;

public interface DocentePagoCalculoService {
BigDecimal calcularPagoPorHoras(
            BigDecimal horasTrabajadas,
            BigDecimal tarifaHora
    );

    BigDecimal calcularDescuentoTardanza(
            Integer minutosTardanza,
            BigDecimal tarifaHora
    );

    BigDecimal calcularDescuentoFaltas(
            Integer faltas,
            BigDecimal tarifaHora,
            BigDecimal factorDescuento
    );

    BigDecimal calcularCumplimiento(
            Integer incumplimientosWhatsapp,
            Integer incumplimientosFicha,
            Integer incumplimientosAgenda
    );

    BigDecimal calcularPagoNeto(
            BigDecimal montoBase,
            BigDecimal descuentoTardanza,
            BigDecimal descuentoFaltas,
            BigDecimal bonificaciones
    );

    PagoCalculoResponseDTO calcularPagoCompleto(PagoCalculoRequestDTO request);

    Pago generarPagoDesdeCalculo(PagoCalculoRequestDTO request);

}
