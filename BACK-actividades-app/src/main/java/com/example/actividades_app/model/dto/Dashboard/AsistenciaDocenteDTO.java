package com.example.actividades_app.model.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaDocenteDTO {
    private String nombreDocente;
    private String especialidad;
    private String horaEntrada;
    private String horaSalida;
    private String estado; // Puntual, Tarde, Ausente
    private String observaciones;

    // Constructor, getters y setters
}
