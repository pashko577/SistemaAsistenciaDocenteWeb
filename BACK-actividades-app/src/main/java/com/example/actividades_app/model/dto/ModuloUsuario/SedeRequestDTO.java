package com.example.actividades_app.model.dto.ModuloUsuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SedeRequestDTO {

    @NotBlank(message = "El nombre de la Sede es obligatorio")
    private String nombreSede;
}
