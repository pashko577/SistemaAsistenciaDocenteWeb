package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsistenciaDiariaDTO {
    private Long cronogramaDiarioId;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private String docenteNombre;
    private String cursoNombre;
    private String gradoSeccion;
    private String nivelNombre; // Vital para agrupar (Sociales, Primaria, etc.)
    private String aula;
    private String estadoClase;
    private String tema;
    // Datos de asistencia (si ya se registró)
    private LocalTime horaEntradaReal;
    private Integer minutosTardanza;
    private String estadoAsistencia;
}
