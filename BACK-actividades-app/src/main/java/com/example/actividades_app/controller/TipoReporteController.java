package com.example.actividades_app.controller;

import com.example.actividades_app.model.dto.Reporte.TipoReporteRequestDTO;
import com.example.actividades_app.model.dto.Reporte.TipoReporteResponseDTO;
import com.example.actividades_app.service.TipoReporteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-reporte")
@RequiredArgsConstructor
public class TipoReporteController {

    private final TipoReporteService tipoReporteService;

    @PostMapping
    public ResponseEntity<TipoReporteResponseDTO> crear(@Valid @RequestBody TipoReporteRequestDTO dto) {
        TipoReporteResponseDTO response = tipoReporteService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TipoReporteResponseDTO>> listar() {
        return ResponseEntity.ok(tipoReporteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoReporteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoReporteService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tipoReporteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}