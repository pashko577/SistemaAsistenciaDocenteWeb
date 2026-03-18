package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioResponseDTO;
import com.example.actividades_app.service.CronogramaDiarioService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/cronogramas-diarios")
@RequiredArgsConstructor
public class CronogramaDiarioController {

    private final CronogramaDiarioService cronogramaService;

    @PostMapping
    public ResponseEntity<CronogramaDiarioResponseDTO> crear(
            @Valid @RequestBody CronogramaDiarioRequestDTO dto) {
        CronogramaDiarioResponseDTO response = cronogramaService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CronogramaDiarioResponseDTO>> listar() {
        return ResponseEntity.ok(cronogramaService.listar());
    }

    @PutMapping("/{id}")
public ResponseEntity<CronogramaDiarioResponseDTO> actualizar(
        @PathVariable Long id,
        @RequestBody CronogramaDiarioRequestDTO dto) {

    return ResponseEntity.ok(cronogramaService.actualizar(id, dto));
}
}