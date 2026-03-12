package com.example.actividades_app.service;

import java.time.LocalDate;

import com.example.actividades_app.model.dto.Pago.PlanillaAdministrativoDTO;



public interface PlanillaAdministrativoService {

    PlanillaAdministrativoDTO calcularPlanilla(
            Long administrativoId,
            LocalDate inicio,
            LocalDate fin
    );
}