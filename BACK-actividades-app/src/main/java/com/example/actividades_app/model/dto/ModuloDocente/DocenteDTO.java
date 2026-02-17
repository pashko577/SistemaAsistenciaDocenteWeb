package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocenteDTO {
    private Long id;
    private String nombreCompleto;   // tomado de Persona vinculada al Usuario
    private String dni;
    private String especialidad;
    private String estado;           // Activo, Inactivo, Vacaciones
    private String observaciones;

    // Constructor, getters y setters
}
