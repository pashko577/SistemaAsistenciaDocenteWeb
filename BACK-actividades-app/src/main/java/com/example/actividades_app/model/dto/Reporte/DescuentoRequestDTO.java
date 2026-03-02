package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class DescuentoRequestDTO {

    // si usas TipoDeduccion (ESSALUD, RETENCION, etc.)
    @NotNull(message = "El tipo de descuento es obligatorio")
    private Long tipoDeduccionId;


    private String observaciones;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
}
