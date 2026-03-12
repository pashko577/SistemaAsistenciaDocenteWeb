package com.example.actividades_app.model.dto.Pago;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.BonificacionRequestDTO;
import com.example.actividades_app.model.dto.Reporte.DescuentoRequestDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class PagoRequestDTO {
    @NotNull
    private LocalDate fecha;
    @NotNull
    private Long contratoId;

    private List<AdelantoRequestDTO> adelantos;
    private List<BonificacionRequestDTO> bonificaciones;
    private List<DescuentoRequestDTO> deducciones;
}
