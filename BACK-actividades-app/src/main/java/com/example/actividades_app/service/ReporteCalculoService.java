package com.example.actividades_app.service;

import java.math.BigDecimal;

import com.example.actividades_app.model.dto.Reporte.CalculoReporteRequestDTO;
import com.example.actividades_app.model.dto.Reporte.CalculoReporteResponseDTO;

public interface ReporteCalculoService {

    CalculoReporteResponseDTO calcularReporte(CalculoReporteRequestDTO request);

    BigDecimal obtenerTarifaHoraReal(CalculoReporteRequestDTO request);

    BigDecimal calcularPagoBase(CalculoReporteRequestDTO request, BigDecimal tarifaHoraReal);

    BigDecimal calcularDescuentoTardanza(Integer sumaMinutos,
            Integer diasConClase,
            BigDecimal tarifaHora);

    BigDecimal calcularDescuentoFaltas(Integer faltas,
            BigDecimal tarifaHora);

    BigDecimal calcularDescuentoCriterios(Integer whatsapp,
            Integer ficha,
            Integer agenda,
            BigDecimal tarifaHora);
}
