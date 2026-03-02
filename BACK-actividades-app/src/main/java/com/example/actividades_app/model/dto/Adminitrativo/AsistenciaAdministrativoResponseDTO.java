package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AsistenciaAdministrativoResponseDTO {

    private Long id;
    private LocalTime horaIngreso;
    private LocalTime horaSalida;
    private LocalTime salidaAlmuerzo;
    private LocalTime retornoAlmuerzo;

    private LocalDate fecha;
    private String observaciones;
    private Integer tardanza;
    private Boolean terno;

    private Long administrativoId;
    private Long cronogramaAdministrativoId;
}

