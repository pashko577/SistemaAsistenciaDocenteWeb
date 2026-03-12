package com.example.actividades_app.model.dto.ModuloDocente;

import com.example.actividades_app.enums.DiaSemana;

import lombok.Data;

@Data
public class CronogramaDocenteRequestDTO {

    private Long asignacionDocenteId;
    private Long horarioBloqueId;
    private DiaSemana diaSemana;

}