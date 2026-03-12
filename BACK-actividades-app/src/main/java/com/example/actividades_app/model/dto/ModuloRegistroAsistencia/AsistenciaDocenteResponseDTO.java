package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;

import java.time.LocalTime;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsistenciaDocenteResponseDTO {

    private Long id;

    private LocalTime horaEntradaDoc;

    private LocalTime horaSalidaDoc;

    private String observacion;

    private Boolean materialClase;

    private Boolean usoTerno;

    private EstadoAsistenciaDocente estadoAsistencia;

    private Long cronogramaDiarioId;

}