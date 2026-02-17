package com.example.actividades_app.model.dto.Dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class AlertaDTO {
    private String titulo;
    private String descripcion;
    private String tipo; // Ej: "Asistencia", "Pago", "Sistema"

    // Constructor, getters y setters
}
