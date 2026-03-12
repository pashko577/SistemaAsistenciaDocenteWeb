package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.model.dto.Contrato.ContratoRequestDTO;
import com.example.actividades_app.model.dto.Contrato.ContratoResponseDTO;
import com.example.actividades_app.service.ContratoService;

@RestController
@RequestMapping("/api/contratos")
@RequiredArgsConstructor
public class ContratoController {

    private final ContratoService contratoService;

    // =========================
    // CREAR CONTRATO
    // =========================
    @PostMapping
    public ResponseEntity<ContratoResponseDTO> crear(
            @RequestBody ContratoRequestDTO dto) {
                
        return ResponseEntity.ok(contratoService.crear(dto));
    }

    // =========================
    // ACTUALIZAR CONTRATO
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody ContratoRequestDTO dto) {

        return ResponseEntity.ok(contratoService.actualizar(id, dto));
    }

    // =========================
    // ELIMINAR CONTRATO
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        contratoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<ContratoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(contratoService.buscarPorId(id));
    }

    // =========================
    // LISTAR CONTRATOS
    // =========================
    @GetMapping
    public ResponseEntity<List<ContratoResponseDTO>> listar() {

        return ResponseEntity.ok(contratoService.listar());
    }

    // =========================
    // BUSCAR POR USUARIO
    // =========================
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<ContratoResponseDTO> buscarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                contratoService.buscarPorUsuario(usuarioId)
        );
    }
}