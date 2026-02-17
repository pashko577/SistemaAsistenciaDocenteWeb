package com.example.actividades_app.model.dto.ModuloCurso;



import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CursoHorarioResponseDTO {

    private Long cronogramaId;

    private LocalDate fecha;

    private LocalTime horaInicio;
    private LocalTime horaFin;

    private String curso;
    private String grado;
    private String seccion;
    private String nivel;
    private String docente;
    private String aula;

}
