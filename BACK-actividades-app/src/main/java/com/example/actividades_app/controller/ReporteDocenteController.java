package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteResponseDTO;
import com.example.actividades_app.service.ReporteDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reportes-docente")
@RequiredArgsConstructor
public class ReporteDocenteController {

    private final ReporteDocenteService reporteService;

    @PostMapping
    public ResponseEntity<ReporteDocenteResponseDTO> registrar(
            @Valid @RequestBody ReporteDocenteRequestDTO dto) {
        ReporteDocenteResponseDTO response = reporteService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReporteDocenteResponseDTO>> listar() {
        return ResponseEntity.ok(reporteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReporteDocenteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(reporteService.obtenerPorId(id));
    }
}