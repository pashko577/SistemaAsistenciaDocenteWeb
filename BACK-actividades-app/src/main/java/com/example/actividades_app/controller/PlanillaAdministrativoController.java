package com.example.actividades_app.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.model.dto.Pago.PlanillaAdministrativoDTO;
import com.example.actividades_app.service.PlanillaAdministrativoService;

@RestController
@RequestMapping("/api/planilla-administrativo")
@RequiredArgsConstructor
public class PlanillaAdministrativoController {

    private final PlanillaAdministrativoService planillaService;

    @GetMapping("/{administrativoId}")
    public PlanillaAdministrativoDTO calcular(
            @PathVariable Long administrativoId,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {

        return planillaService.calcularPlanilla(
                administrativoId,
                inicio,
                fin
        );
    }
}