package com.example.actividades_app.model.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {
    private int totalDocentes;
    private int presentes;
    private int conTardanza;
    private int ausentes;
    private int conPermiso;
    private double porcentajeAsistencia;
    private double totalAPagarMes;

    // Constructor, getters y setters
}
