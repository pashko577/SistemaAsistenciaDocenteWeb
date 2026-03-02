package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;

import java.time.LocalTime;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrarAsistenciaDocenteRequestDTO {

    private Long docenteId;
    private Long cronogramaDiaraioId;
    private LocalTime horaEntradaDoc;
    private LocalTime horaSalidaDoc;
    private String observacion;
    private Boolean materialClase;
    private Boolean usoTerno;
    private EstadoAsistenciaDocente EstadoAsistencia;
}
