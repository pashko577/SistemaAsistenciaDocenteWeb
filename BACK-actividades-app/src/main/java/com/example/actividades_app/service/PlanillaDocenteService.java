package com.example.actividades_app.service;

import java.time.LocalDate;

import com.example.actividades_app.model.dto.ModuloDocente.PlanillaDocenteDTO;

public interface PlanillaDocenteService {

    PlanillaDocenteDTO calcularPlanilla(
            Long docenteId,
            LocalDate inicio,
            LocalDate fin
    );

}