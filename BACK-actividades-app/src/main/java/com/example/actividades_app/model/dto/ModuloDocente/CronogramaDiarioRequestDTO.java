package com.example.actividades_app.model.dto.ModuloDocente;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CronogramaDiarioRequestDTO {

    private Long cronogramaDocenteId;
    private LocalDate fecha;

}
