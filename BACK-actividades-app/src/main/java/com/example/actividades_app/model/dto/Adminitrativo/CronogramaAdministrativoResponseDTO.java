package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CronogramaAdministrativoResponseDTO {



    private Long id;
    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String diaSemana;

    private Long administrativoId;

    private String nombres;
    private String apellidos;
}
