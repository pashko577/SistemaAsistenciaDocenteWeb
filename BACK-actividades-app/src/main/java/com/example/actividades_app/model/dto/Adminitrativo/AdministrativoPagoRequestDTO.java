package com.example.actividades_app.model.dto.Adminitrativo;

import java.math.BigDecimal;
import java.time.LocalTime;

import com.example.actividades_app.model.Entity.Pago;

import lombok.Data;

@Data
public class AdministrativoPagoRequestDTO {

    private Long usuarioId;

    private Pago.TipoPago tipoPago;

    private BigDecimal sueldoMensual;

    private BigDecimal tarifaHora;

    private LocalTime horaIngreso;

    private LocalTime horaSalida;

    private LocalTime salidaAlmuerzo;

    private LocalTime retornoAlmuerzo;

    private Integer minutosTardanza;

    private Integer minutosLaboralesMes;

}