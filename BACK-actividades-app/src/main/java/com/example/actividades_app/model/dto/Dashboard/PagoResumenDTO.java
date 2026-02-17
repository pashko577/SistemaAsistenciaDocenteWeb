package com.example.actividades_app.model.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PagoResumenDTO {
    private double totalMes;
    private int cantidadPagos;
    private String mes; // Ej: "Febrero 2026"

    // Constructor, getters y setters
}
