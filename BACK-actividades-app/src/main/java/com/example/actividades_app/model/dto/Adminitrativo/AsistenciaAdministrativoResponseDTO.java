package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.actividades_app.enums.TipoAsistencia;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
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

    private TipoAsistencia tipoAsistencia;
    private Long administrativoId;
    private Long cronogramaAdministrativoId;
}

