package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;


import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteDocenteRequestDTO {

    private Long docenteId;
    private LocalDate fecha;

    private String tema;
    private String observaciones;

    private int tardanza;

    private Long tipoAsistenciaId;

}
