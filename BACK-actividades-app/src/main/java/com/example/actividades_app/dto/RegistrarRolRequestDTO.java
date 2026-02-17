package com.example.actividades_app.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegistrarRolRequestDTO {

    @NotBlank(message = "El nombre del rol es obligatorio")
    private String name;
}
