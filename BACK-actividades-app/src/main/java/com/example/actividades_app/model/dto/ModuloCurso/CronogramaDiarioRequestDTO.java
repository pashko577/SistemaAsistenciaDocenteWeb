package com.example.actividades_app.model.dto.ModuloCurso;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class CronogramaDiarioRequestDTO {

    private LocalDate fecha;

    private LocalTime horaInicioClase;
    private LocalTime horaFinClase;

    private Long claseId;

}
