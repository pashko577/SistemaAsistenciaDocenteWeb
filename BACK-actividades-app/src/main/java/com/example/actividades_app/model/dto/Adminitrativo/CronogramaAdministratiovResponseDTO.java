package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class CronogramaAdministratiovResponseDTO {



    private Long id;

    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private LocalDate fecha;

    private Long administrativoId;
}
