package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdelantoRequestDTO {


    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
}
