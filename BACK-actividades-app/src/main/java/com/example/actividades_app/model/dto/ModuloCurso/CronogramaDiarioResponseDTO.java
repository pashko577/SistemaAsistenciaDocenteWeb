package com.example.actividades_app.model.dto.ModuloCurso;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CronogramaDiarioResponseDTO {

    private Long id;

    private LocalDate fecha;

    private LocalTime horaInicioClase;
    private LocalTime horaFinClase;

    private String curso;
    private String grado;
    private String seccion;
    private String nivel;
    private String docente;
    private String aula;
    private String tema;

}
