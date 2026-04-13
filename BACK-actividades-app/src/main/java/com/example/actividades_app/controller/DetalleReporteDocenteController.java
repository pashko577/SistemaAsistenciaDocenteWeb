package com.example.actividades_app.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteResponseDTO;
import com.example.actividades_app.service.DetalleReporteDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/detalles-reporte-docente")
@RequiredArgsConstructor
public class DetalleReporteDocenteController {

    private final DetalleReporteDocenteService detalleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DetalleReporteDocenteResponseDTO> registrar(
            @Valid @RequestBody DetalleReporteDocenteRequestDTO dto) {
        DetalleReporteDocenteResponseDTO response = detalleService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/reporte/{reporteId}")
    @IsStaff
    public ResponseEntity<DetalleReporteDocenteResponseDTO> obtenerPorReporte(@PathVariable Long reporteId) {
        return ResponseEntity.ok(detalleService.obtenerPorReporte(reporteId));
    }
}