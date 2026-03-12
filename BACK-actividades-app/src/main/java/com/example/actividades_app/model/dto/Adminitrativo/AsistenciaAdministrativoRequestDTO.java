package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.example.actividades_app.enums.TipoAsistencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaAdministrativoRequestDTO {

    private LocalTime horaIngreso;
    private LocalTime horaSalida;
    private LocalTime salidaAlmuerzo;
    private LocalTime retornoAlmuerzo;

    private LocalDate fecha;
    private String observaciones;
    private Boolean terno;

    private TipoAsistencia tipoAsistencia;

    private Long administrativoId;
    private Long cronogramaAdministrativoId;
}
