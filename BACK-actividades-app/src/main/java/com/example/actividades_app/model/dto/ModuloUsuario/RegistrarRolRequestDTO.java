package com.example.actividades_app.model.dto.ModuloUsuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrarRolRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;
}
