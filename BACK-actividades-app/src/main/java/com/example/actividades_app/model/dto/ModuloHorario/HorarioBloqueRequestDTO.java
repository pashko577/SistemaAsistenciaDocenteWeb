package com.example.actividades_app.model.dto.ModuloHorario;

import java.time.LocalTime;

import lombok.Data;

@Data
public class HorarioBloqueRequestDTO {

    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer ordenBloque;
}
