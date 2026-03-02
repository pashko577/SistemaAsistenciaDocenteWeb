package com.example.actividades_app.model.dto.ModuloCurso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NuevoCursoRequestDTO {

    @NotBlank(message = "El nombre del curso es obligatorio")
    private Long cursoID;
    @NotNull(message = "El nivel educativo es obligatorio")
    private Long nivelID;
    @NotNull(message = "El grado es obligatorio")
    private Long gradoID;
    @NotNull(message = "La sección es obligatoria")
    private Long seccionID;
    @NotBlank(message = "El aula es obligatoria")
    private String aula;
    @NotNull(message ="El docente es obligatorio")
    private Long docenteID;
}
