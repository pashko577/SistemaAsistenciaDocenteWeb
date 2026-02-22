package com.example.actividades_app.service;


import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.actividades_app.model.Entity.Pago;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoPagoRequestDTO;


public interface AdministrativoPagoCalculoService {

    BigDecimal calcularMinutosTrabajados(
            AdministrativoPagoRequestDTO request);

    BigDecimal calcularDescuentoTardanza(
            Integer minutosTardanza,
            BigDecimal valorMinuto);

    BigDecimal calcularPagoPorMinutos(
            BigDecimal minutosTrabajados,
            BigDecimal valorMinuto);

    BigDecimal calcularValorMinutoDesdeSueldoMensual(
            BigDecimal sueldoMensual,
            Integer minutosLaboralesMes);

/*     AdministrativoPagoResponseDTO calcularPagoCompleto(
            AdministrativoPagoRequestDTO request); */

    Pago generarPago(
            AdministrativoPagoRequestDTO request);

}