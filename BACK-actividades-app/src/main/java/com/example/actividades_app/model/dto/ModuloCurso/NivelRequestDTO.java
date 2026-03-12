package com.example.actividades_app.model.dto.ModuloCurso;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NivelRequestDTO {

    @NotBlank(message = "El nombre del nivel es obligatorio")
    @Size(max = 50)
    private String nomNivel;
}
