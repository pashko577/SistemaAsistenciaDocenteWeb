package com.example.actividades_app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoResponseDTO;
import com.example.actividades_app.service.AsistenciaAdministrativoService;

@RestController
@RequestMapping("/api/asistencia-administrativo")
@RequiredArgsConstructor
public class AsistenciaAdministrativoController {

    private final AsistenciaAdministrativoService asistenciaService;

    // =========================
    // REGISTRAR ASISTENCIA
    // =========================
    @PostMapping
    @IsStaff
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> registrar(
            @RequestBody AsistenciaAdministrativoRequestDTO dto) {

        return ResponseEntity.ok(asistenciaService.registrar(dto));
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    @IsStaff
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody AsistenciaAdministrativoRequestDTO dto) {

        return ResponseEntity.ok(asistenciaService.actualizar(id, dto));
    }

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    @IsStaff
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        asistenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(asistenciaService.buscarPorId(id));
    }

    // =========================
    // LISTAR POR ADMINISTRATIVO
    // =========================
    @GetMapping("/administrativo/{administrativoId}")
    @IsStaff
    public ResponseEntity<List<AsistenciaAdministrativoResponseDTO>> listarPorAdministrativo(
            @PathVariable Long administrativoId) {

        return ResponseEntity.ok(
                asistenciaService.listarPorAdministrativo(administrativoId));
    }

    // Agrega esto a AsistenciaAdministrativoController.java

    @GetMapping("/fecha/{fecha}")
    @IsStaff
    public ResponseEntity<List<AsistenciaAdministrativoResponseDTO>> listarPorFecha(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        // Nota: Necesitas agregar este método a tu Service y Repository de Java
        return ResponseEntity.ok(asistenciaService.listarPorFecha(fecha));
    }

    // =========================
    // LISTAR POR PERIODO
    // =========================
    @GetMapping("/periodo/{administrativoId}")
    @IsStaff
    public ResponseEntity<List<AsistenciaAdministrativoResponseDTO>> listarPorPeriodo(
            @PathVariable Long administrativoId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {

        return ResponseEntity.ok(
                asistenciaService.listarPorPeriodo(administrativoId, inicio, fin));
    }

}