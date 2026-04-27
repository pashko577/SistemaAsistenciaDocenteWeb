package com.example.actividades_app.model.dto.ModuloHorario;

import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HorarioBloqueResponseDTO {

    private Long id;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Integer ordenBloque;

    private Long nivelId;
    private String nivelNombre;

}