package com.example.actividades_app.model.dto.Permisos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ModuloRequestDTO {

    @NotBlank(message = "El nombre del módulo es obligatorio")
    @Size(max = 100)
    private String nombre;

    @Size(max = 255)
    private String descripcion;

    @Size(max = 100)
    private String ruta;
}