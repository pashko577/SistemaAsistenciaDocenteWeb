package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteResponseDTO;
import com.example.actividades_app.service.CronogramaDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cronogramas-docente")
@RequiredArgsConstructor
public class CronogramaDocenteController {

    private final CronogramaDocenteService cronogramaService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CronogramaDocenteResponseDTO> crear(
            @Valid @RequestBody CronogramaDocenteRequestDTO dto) {
        CronogramaDocenteResponseDTO response = cronogramaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<CronogramaDocenteResponseDTO>> listar() {
        return ResponseEntity.ok(cronogramaService.listar());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<CronogramaDocenteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cronogramaService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        cronogramaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}