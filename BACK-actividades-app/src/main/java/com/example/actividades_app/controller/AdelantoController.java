package com.example.actividades_app.controller;


import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoResponseDTO;
import com.example.actividades_app.service.AdelantoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/adelantos")
@RequiredArgsConstructor
public class AdelantoController {

    private final AdelantoService adelantoService;

    @PostMapping("/registrar")
    @IsStaff
    public ResponseEntity<AdelantoResponseDTO> registrar(@Valid @RequestBody AdelantoRequestDTO request) {
        return new ResponseEntity<>(adelantoService.registrar(request), HttpStatus.CREATED);
    }
@PutMapping("/actualizar/{id}")
@IsStaff
public ResponseEntity<AdelantoResponseDTO> actualizar(@PathVariable Long id, @RequestBody AdelantoRequestDTO request) {
    return ResponseEntity.ok(adelantoService.actualizar(id, request));
}
    @GetMapping("/todos")
    @IsStaff
    public ResponseEntity<List<AdelantoResponseDTO>> listarTodos() {
        return ResponseEntity.ok(adelantoService.listarTodos());
    }

    // Endpoint crítico para el Módulo de Pagos: Obtiene adelantos sin cobrar de un usuario
    @GetMapping("/pendientes/usuario/{usuarioId}")
    @IsStaff
    public ResponseEntity<List<AdelantoResponseDTO>> listarPendientes(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(adelantoService.listarPendientesPorUsuario(usuarioId));
    }

    // Endpoint para mostrar el total acumulado a descontar en la interfaz de planilla
    @GetMapping("/total-pendiente/usuario/{usuarioId}")
    @IsStaff
    public ResponseEntity<BigDecimal> obtenerTotalPendiente(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(adelantoService.calcularTotalPendiente(usuarioId));
    }

    @PutMapping("/anular/{id}")
    @IsStaff
    public ResponseEntity<Void> anular(@PathVariable Long id) {
        adelantoService.anularAdelanto(id);
        return ResponseEntity.noContent().build();
    }
}