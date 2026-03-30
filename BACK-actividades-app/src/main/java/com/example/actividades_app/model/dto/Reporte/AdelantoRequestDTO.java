package com.example.actividades_app.model.dto.Reporte;

import java.math.BigDecimal;

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

    // Por defecto suele ser PENDIENTE al crear, 
    // pero lo incluimos por si se requiere editar el estado.
    private EstadoAdelanto estado;
}