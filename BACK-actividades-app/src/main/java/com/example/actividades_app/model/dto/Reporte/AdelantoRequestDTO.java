package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.example.actividades_app.enums.EstadoAdelanto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdelantoRequestDTO {

    @NotBlank(message = "El nombre o motivo del adelanto es obligatorio")
    private String nombre;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a cero")
    private BigDecimal monto;

    @NotNull(message = "Debe especificar el usuario (ID)")
    private Long usuarioId;

    // NUEVO: Para definir cuándo se dio el adelanto
    @NotNull(message = "La fecha del adelanto es obligatoria")
    private LocalDate fechaCreacion; 

    private EstadoAdelanto estado;
}