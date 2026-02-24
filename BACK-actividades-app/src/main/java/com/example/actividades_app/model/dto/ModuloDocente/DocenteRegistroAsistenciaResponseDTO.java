package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.Data;

@Data
public class DocenteRegistroAsistenciaResponseDTO {
    private Long reporteId;
    private String fecha;
    private String hora; //1RA, 2DA, 3RA ETC
    private String grado;
    private String curso;
    private String tema;
    private Integer horaEfectiva;
    private String observaciones;
    private Integer tardanzaMinutos;
    private String estado;
    private String tipoAsistencia; // FK a TipoAsistencia
    private String tipoReporte;
}
