package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class DescuentoResponseDTO {
    
    private Long detalledescuentoId;
    private String nombre;
    private BigDecimal monto;

}
