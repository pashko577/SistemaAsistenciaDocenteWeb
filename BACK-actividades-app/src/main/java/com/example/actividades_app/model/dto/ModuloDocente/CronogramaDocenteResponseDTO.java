package com.example.actividades_app.model.dto.ModuloDocente;

import com.example.actividades_app.enums.DiaSemana;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CronogramaDocenteResponseDTO {

    private Long id;

    private Long asignacionDocenteId;

    private Long horarioBloqueId;
    private String horaInicio;
    private String horaFin;

    private DiaSemana diaSemana;

}