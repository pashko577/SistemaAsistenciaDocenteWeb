package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteResponseDTO;
import com.example.actividades_app.service.AsignacionDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asignacion-docente")
@RequiredArgsConstructor
public class AsignacionDocenteController {
private final AsignacionDocenteService asignacionService;

    @PostMapping
    public ResponseEntity<AsignacionDocenteResponseDTO> registrar(
            @Valid @RequestBody AsignacionDocenteRequestDTO dto) {
        AsignacionDocenteResponseDTO response = asignacionService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AsignacionDocenteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(asignacionService.obtenerPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<AsignacionDocenteResponseDTO>> listar() {
        return ResponseEntity.ok(asignacionService.listar());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AsignacionDocenteResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AsignacionDocenteRequestDTO dto) {
        return ResponseEntity.ok(asignacionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asignacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}