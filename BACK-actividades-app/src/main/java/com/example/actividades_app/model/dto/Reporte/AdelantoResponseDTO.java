package com.example.actividades_app.model.dto.Reporte;



import java.math.BigDecimal;
import lombok.Data;

@Data
public class AdelantoResponseDTO {

    private Long adelantoId;
    private String nombre;
    private BigDecimal monto;
}