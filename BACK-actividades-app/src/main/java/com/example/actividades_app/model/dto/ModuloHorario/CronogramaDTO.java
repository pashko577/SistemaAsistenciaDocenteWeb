package com.example.actividades_app.model.dto.ModuloHorario;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CronogramaDTO {
    private Long claseId;
    private String docente;
    private String curso;
    private String grado;
    private String seccion;
    private String nivel;
    private LocalDate fecha;
    private String horaInicio;
    private String horaFin;

    // Constructor, getters y setters
}
