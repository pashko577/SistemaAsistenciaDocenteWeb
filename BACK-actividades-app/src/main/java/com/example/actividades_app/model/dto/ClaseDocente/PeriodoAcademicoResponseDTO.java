package com.example.actividades_app.model.dto.ClaseDocente;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PeriodoAcademicoResponseDTO {

    private Long id;

    private String nombre;

    private LocalDate fechaInicio;

    private LocalDate fechaFin;

}