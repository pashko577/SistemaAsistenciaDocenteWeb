package com.example.actividades_app.model.dto.ModuloDocente;

import java.time.LocalDate;

import com.example.actividades_app.model.Entity.CronogramaDiario.EstadoClase;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CronogramaDiarioResponseDTO {

    private Long id;

    private LocalDate fecha;

    private EstadoClase estadoClase;

    private Long cronogramaDocenteId;

}
