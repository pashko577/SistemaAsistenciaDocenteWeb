package com.example.actividades_app.model.dto.ModuloCurso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GradoRequestDTO {


    @NotBlank(message = "El número de grado es obligatorio")
    private String numGrado;

    @NotNull(message = "Debe seleccionar un nivel")
    private Long nivelId;
}