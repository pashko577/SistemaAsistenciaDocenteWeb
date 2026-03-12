package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;

import java.time.LocalTime;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;

import lombok.Data;

@Data
public class AsistenciaDocenteRequestDTO {

    private LocalTime horaEntradaDoc;
    private LocalTime horaSalidaDoc;

    private String observacion;
    private Boolean materialClase;

    private Boolean usoTerno;

    private EstadoAsistenciaDocente estadoAsistencia;

    private Long docenteId;
    private Long cronogramaDiarioId;

}
