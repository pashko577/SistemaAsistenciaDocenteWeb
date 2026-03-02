package com.example.actividades_app.controller;

import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoResponseDTO;
import com.example.actividades_app.service.AsistenciaAdministrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/asistencia-administrativo")
@RequiredArgsConstructor
public class AsistenciaAdministrativoController {

    private final AsistenciaAdministrativoService service;

    // REGISTRAR ASISTENCIA
    @PostMapping
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> registrar(
            @Valid @RequestBody AsistenciaAdministrativoRequestDTO dto) {

        AsistenciaAdministrativoResponseDTO response = service.registrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ACTUALIZAR ASISTENCIA
    @PutMapping("/{id}")
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AsistenciaAdministrativoRequestDTO dto) {

        AsistenciaAdministrativoResponseDTO response = service.actualizar(id, dto);

        return ResponseEntity.ok(response);
    }

    // ELIMINAR
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        service.eliminar(id);

        return ResponseEntity.noContent().build();
    }

    // =====================================
    // BUSCAR POR ID
    // =====================================
    @GetMapping("/{id}")
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // =====================================
    // LISTAR POR ADMINISTRATIVO
    // =====================================
    @GetMapping("/administrativo/{administrativoId}")
    public ResponseEntity<List<AsistenciaAdministrativoResponseDTO>> listarPorAdministrativo(
            @PathVariable Long administrativoId) {

        return ResponseEntity.ok(
                service.listarPorAdministrativo(administrativoId));
    }

    // =====================================
    // BUSCAR POR ADMINISTRATIVO + FECHA
    // =====================================
    @GetMapping("/administrativo/{administrativoId}/fecha")
    public ResponseEntity<AsistenciaAdministrativoResponseDTO> buscarPorAdministrativoYFecha(
            @PathVariable Long administrativoId,
            @RequestParam LocalDate fecha) {

        return ResponseEntity.ok(
                service.buscarPorAdministrativoYFecha(administrativoId, fecha));
    }
}