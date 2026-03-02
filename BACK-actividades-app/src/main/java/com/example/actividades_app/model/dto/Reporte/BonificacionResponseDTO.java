package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class BonificacionResponseDTO {

    private Long detallebonificacionId;
    private String nombre;
    private BigDecimal monto;

}
