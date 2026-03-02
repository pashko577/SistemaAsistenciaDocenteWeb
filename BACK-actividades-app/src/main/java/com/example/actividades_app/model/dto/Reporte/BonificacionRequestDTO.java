package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BonificacionRequestDTO {

    @NotBlank(message = "El nombre de la bonificación es obligatorio")
    private String nombre;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
}
