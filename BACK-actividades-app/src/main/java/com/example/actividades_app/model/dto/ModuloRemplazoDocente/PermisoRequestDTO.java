package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermisoRequestDTO {
    @NotNull(message = "El docente es obligatorio")
    private Long docenteId;

    @NotNull(message = "La fecha del permiso es obligatoria")
    private String fecha;

    @NotBlank(message = "El motivo del permiso es obligatorio")
    private String motivo;

    @NotBlank(message = "El tipo de permiso es obligatorio")
    private String tipo; // Ej: "Permiso médico", "Capacitación", "Asuntos personales"
}
