package com.example.actividades_app.model.dto.Permisos;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RolModuloRequestDTO {

    @NotNull(message = "El rol es obligatorio")
    private Long rolId;

    @NotNull(message = "El módulo es obligatorio")
    private Long moduloId;
}