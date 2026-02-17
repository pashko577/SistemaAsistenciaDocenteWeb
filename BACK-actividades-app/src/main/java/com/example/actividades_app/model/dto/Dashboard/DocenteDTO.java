package com.example.actividades_app.model.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class DocenteDTO {
    private String nombreCompleto;
    private String especialidad;
    private String estado; // Activo/Inactivo
    private String sede;

    // Constructor, getters y setters
}
