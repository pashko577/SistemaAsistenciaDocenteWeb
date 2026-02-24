package com.example.actividades_app.model.dto.Adminitrativo;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaAdministrativoRequestDTO {

    private LocalTime horaEntrada;
    private LocalTime horaSalida;
    private LocalDate fecha;

    private Long administrativoId;
}