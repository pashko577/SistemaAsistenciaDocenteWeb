package com.example.actividades_app.model.dto.Reporte;



import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.actividades_app.enums.EstadoAdelanto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdelantoResponseDTO {

    private Long id;
    private String nombre;
    private BigDecimal monto;
    private EstadoAdelanto estado;

    
    // Información simplificada del Usuario/Persona
    private Long usuarioId;
    private String nombreCompletoPersonal;
    private String dniPersonal;

    // Información del Pago (si ya fue aplicado)
    private Long pagoId;
    private LocalDateTime fechaPago; // Para saber cuándo se descontó

}