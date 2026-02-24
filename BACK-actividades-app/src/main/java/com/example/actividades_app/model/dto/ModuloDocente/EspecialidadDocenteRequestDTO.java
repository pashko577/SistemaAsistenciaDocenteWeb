package com.example.actividades_app.model.dto.ModuloDocente;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class EspecialidadDocenteRequestDTO {
    @NotBlank(message = "El nombre de la Especialidad es obligatorio")
    private String nombreEspecialidad;
}
