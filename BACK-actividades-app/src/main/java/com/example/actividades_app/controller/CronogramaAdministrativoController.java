package com.example.actividades_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;
import com.example.actividades_app.service.CronogramaAdministrativoService;

import java.util.List;

@RestController
@RequestMapping("/api/cronogramas-administrativos")
@RequiredArgsConstructor
public class CronogramaAdministrativoController {

    private final CronogramaAdministrativoService service;

    // =========================
    // CREAR
    // =========================
    @PostMapping
    public CronogramaAdministrativoResponseDTO crear(
            @RequestBody CronogramaAdministrativoRequestDTO dto) {
        return service.crear(dto);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    public CronogramaAdministrativoResponseDTO actualizar(
            @PathVariable Long id,
            @RequestBody CronogramaAdministrativoRequestDTO dto) {
        return service.actualizar(id, dto);
    }

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public CronogramaAdministrativoResponseDTO buscarPorId(
            @PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // =========================
    // LISTAR POR ADMINISTRATIVO
    // =========================
    @GetMapping("/administrativo/{administrativoId}")
    public List<CronogramaAdministrativoResponseDTO>
    listarPorAdministrativo(@PathVariable Long administrativoId) {

        return service.listarPorAdministrativo(administrativoId);
    }

    // =========================
    // LISTAR POR DIA
    // =========================
    @GetMapping("/dia/{diaSemana}")
    public List<CronogramaAdministrativoResponseDTO>
    listarPorDiaSemana(
            @PathVariable CronogramaAdministrativo.DiaSemana diaSemana) {

        return service.listarPorDiaSemana(diaSemana);
    }

    // =========================
    // BUSCAR ADMIN + DIA
    // =========================
    @GetMapping("/administrativo/{administrativoId}/dia/{diaSemana}")
    public CronogramaAdministrativoResponseDTO
    buscarPorAdministrativoYDiaSemana(
            @PathVariable Long administrativoId,
            @PathVariable CronogramaAdministrativo.DiaSemana diaSemana) {

        return service.buscarPorAdministrativoYDiaSemana(
                administrativoId,
                diaSemana);
    }
}