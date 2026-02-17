package com.example.actividades_app.model.dto.Dashboard;

import lombok.Data;

@Data
public class ResumenAsistenciaDTO {
    private int totalDocentes;
    private int presentes;
    private int conTardanza;
    private int ausentes;
    private int conPermiso;
    private double porcentajeAsistencia;

    // Constructor, getters y setters
}
