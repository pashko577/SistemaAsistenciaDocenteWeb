package com.example.actividades_app.model.dto.Permisos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RolModuloResponseDTO {

    private Long id;

    private Long rolId;
    private String rolNombre;

    private Long moduloId;
    private String moduloNombre;
}