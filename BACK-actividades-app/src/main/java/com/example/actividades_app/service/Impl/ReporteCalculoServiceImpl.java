package com.example.actividades_app.service.Impl;


import org.springframework.stereotype.Service;

import com.example.actividades_app.model.dto.Reporte.CalculoReporteRequestDTO;
import com.example.actividades_app.model.dto.Reporte.CalculoReporteResponseDTO;
import com.example.actividades_app.service.ReporteCalculoService;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class ReporteCalculoServiceImpl implements ReporteCalculoService {

    @Override
    public CalculoReporteResponseDTO calcularReporte(CalculoReporteRequestDTO request) {

        BigDecimal tarifaHoraReal = obtenerTarifaHoraReal(request);

        BigDecimal pagoBase = calcularPagoBase(request, tarifaHoraReal);

        BigDecimal descuentoTardanza = calcularDescuentoTardanza(
                request.getSumaMinutosTardanza(),
                request.getCantidadDiasConClase(),
                tarifaHoraReal
        );

        BigDecimal descuentoFaltas = calcularDescuentoFaltas(
                request.getCantidadFaltas(),
                tarifaHoraReal
        );

        BigDecimal descuentoCriterios = calcularDescuentoCriterios(
                request.getIncumplimientosWhatsapp(),
                request.getIncumplimientosFicha(),
                request.getIncumplimientosAgenda(),
                tarifaHoraReal
        );

        BigDecimal totalFinal = pagoBase
                .subtract(descuentoTardanza)
                .subtract(descuentoFaltas)
                .subtract(descuentoCriterios)
                .setScale(2, RoundingMode.HALF_UP);

        return CalculoReporteResponseDTO.builder()
                .pagoBase(pagoBase)
                .descuentoTardanza(descuentoTardanza)
                .descuentoFaltas(descuentoFaltas)
                .descuentoCriterios(descuentoCriterios)
                .totalFinal(totalFinal)
                .build();
    }

    // =====================================================
    // 1️⃣ OBTENER TARIFA REAL (hora o mensual)
    // =====================================================
    @Override
    public BigDecimal obtenerTarifaHoraReal(CalculoReporteRequestDTO request) {

        if ("POR_HORA".equalsIgnoreCase(request.getTipoPago())) {
            return request.getTarifaPorHora();
        }

        return request.getSueldoMensual()
                .divide(
                        BigDecimal.valueOf(request.getHorasMensualesReferencia()),
                        4,
                        RoundingMode.HALF_UP
                );
    }

    // =====================================================
    // 2️⃣ PAGO BASE
    // =====================================================
    @Override
    public BigDecimal calcularPagoBase(CalculoReporteRequestDTO request,
                                       BigDecimal tarifaHoraReal) {

        if ("POR_HORA".equalsIgnoreCase(request.getTipoPago())) {

            return tarifaHoraReal
                    .multiply(BigDecimal.valueOf(request.getHorasDictadas()))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return request.getSueldoMensual()
                .setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // 3️⃣ DESCUENTO TARDANZA (Replica exacta Excel)
    // =====================================================
    @Override
    public BigDecimal calcularDescuentoTardanza(Integer sumaMinutos,
                                                Integer diasConClase,
                                                BigDecimal tarifaHora) {

        if (sumaMinutos == null || diasConClase == null)
            return BigDecimal.ZERO;

        int tolerancia = diasConClase * 3;
        int minutosReales = sumaMinutos - tolerancia;

        if (minutosReales <= 0) {
            return BigDecimal.ZERO;
        }

        if (minutosReales >= 11) {

            BigDecimal horasEquivalentes = BigDecimal.valueOf(minutosReales)
                    .divide(BigDecimal.valueOf(45), 4, RoundingMode.HALF_UP);

            return horasEquivalentes
                    .multiply(tarifaHora)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    // =====================================================
    // 4️⃣ DESCUENTO FALTAS
    // =====================================================
    @Override
    public BigDecimal calcularDescuentoFaltas(Integer faltas,
                                              BigDecimal tarifaHora) {

        if (faltas == null) return BigDecimal.ZERO;

        return tarifaHora
                .multiply(BigDecimal.valueOf(faltas))
                .multiply(BigDecimal.valueOf(0.5))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // 5️⃣ DESCUENTO CRITERIOS
    // Replica fórmula Excel 1 y 2
    // =====================================================
    @Override
    public BigDecimal calcularDescuentoCriterios(Integer whatsapp,
                                                 Integer ficha,
                                                 Integer agenda,
                                                 BigDecimal tarifaHora) {

        whatsapp = whatsapp == null ? 0 : whatsapp;
        ficha = ficha == null ? 0 : ficha;
        agenda = agenda == null ? 0 : agenda;

        double base = (whatsapp * 0)
                + (ficha * 0.3)
                + (agenda * 0.5);

        if (base >= 11) {

            double penalizacion = (whatsapp * 0)
                    + (ficha * 0.1)
                    + (agenda * 0.5);

            return BigDecimal.valueOf(penalizacion)
                    .multiply(tarifaHora)
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }
}
