package com.example.actividades_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.actividades_app.enums.TipoPlanilla;
import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;
import com.example.actividades_app.service.TipoActividadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tipo-actividad")
@RequiredArgsConstructor
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoActividadResponseDTO> crear(@Valid @RequestBody TipoActividadRequestDTO dto) {
        TipoActividadResponseDTO response = tipoActividadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoActividadResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoActividadService.buscarPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TipoActividadResponseDTO>> listar() {
        return ResponseEntity.ok(tipoActividadService.listar());
    }
    @GetMapping("/planilla/{planilla}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TipoActividadResponseDTO>> listarPorPlanilla(@PathVariable TipoPlanilla planilla) {
        return ResponseEntity.ok(tipoActividadService.listarPorPlanilla(planilla));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoActividadResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoActividadRequestDTO dto) {
        return ResponseEntity.ok(tipoActividadService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoActividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
