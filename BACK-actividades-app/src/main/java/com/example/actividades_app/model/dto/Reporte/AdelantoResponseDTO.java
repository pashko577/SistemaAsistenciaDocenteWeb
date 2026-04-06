package com.example.actividades_app.model.dto.Reporte;



import java.math.BigDecimal;
import java.time.LocalDate;
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
    
    // NUEVO: Para mostrar en la tabla de adelantos
    private LocalDate fechaCreacion; 

    private Long usuarioId;
    private String nombreCompletoPersonal;
    private String dniPersonal;

    private Long pagoId;
    private LocalDate fechaPago; // Cambiado a LocalDate para consistencia
}