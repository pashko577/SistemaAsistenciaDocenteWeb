package com.example.actividades_app.model.dto.Adminitrativo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaAdministrativoRequestDTO {

    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private String diaSemana;

    private Long administrativoId;
}