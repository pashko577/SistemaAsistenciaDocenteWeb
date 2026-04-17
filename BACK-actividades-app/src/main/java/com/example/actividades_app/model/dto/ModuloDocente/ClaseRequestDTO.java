package com.example.actividades_app.model.dto.ModuloDocente;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClaseRequestDTO {
    @NotNull(message = "El tiempo de clase es obligatorio")
    @Positive
    private Integer tiempoClase;

    @Size(max = 50, message = "El aula debe tener máximo 50 caracteres")
    private String aula;

    @NotNull(message = "El curso es obligatorio")
    private Long cursoId;

    @NotNull(message = "La sección es obligatoria")
    private Long seccionId;

    @NotNull(message = "El periodo académico es obligatorio")
    private Long periodoAcademicoId;
}
