package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegistrarAsistenciaDocenteResponseDTO {
    private Long asistenciaId;
    private String fecha;
    private String horaClase;
    private String horaEntradaDoc;
    private String horaSalidaDoc;
    private Integer diferenciaMinutos;
    private EstadoAsistenciaDocente estadoAsistencia; // ENUM directo
    private String nombreDocente;
    private String curso;
    private String grado;
    private String seccion;
    private Boolean materialClase;
    private Boolean usoTerno;
    private String observacion;
}
