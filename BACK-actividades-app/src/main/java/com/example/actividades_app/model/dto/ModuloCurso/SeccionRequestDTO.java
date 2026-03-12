package com.example.actividades_app.model.dto.ModuloCurso;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SeccionRequestDTO {

    @NotBlank(message = "El nombre de la sección es obligatorio")
    private String nomSeccion;

    @NotNull(message = "Debe seleccionar un grado")
    private Long gradoId;
}
