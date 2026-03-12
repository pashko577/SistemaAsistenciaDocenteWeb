package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DescuentoResponseDTO {

    private Long tipoDeduccionId;
    private String observaciones;
    private BigDecimal monto;

}