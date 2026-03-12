package com.example.actividades_app.controller;

import com.example.actividades_app.model.dto.ModuloDocente.ClaseRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ClaseResponseDTO;
import com.example.actividades_app.service.ClaseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clases")
@RequiredArgsConstructor
public class ClaseController {

    private final ClaseService claseService;

    // Crear clase
    @PostMapping
    public ResponseEntity<ClaseResponseDTO> crear(@Valid @RequestBody ClaseRequestDTO dto) {
        ClaseResponseDTO response = claseService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Listar todas las clases
    @GetMapping
    public ResponseEntity<List<ClaseResponseDTO>> listar() {
        return ResponseEntity.ok(claseService.listar());
    }

    // Obtener clase por id
    @GetMapping("/{id}")
    public ResponseEntity<ClaseResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(claseService.obtenerPorId(id));
    }

    // Actualizar clase
    @PutMapping("/{id}")
    public ResponseEntity<ClaseResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody ClaseRequestDTO dto) {
        return ResponseEntity.ok(claseService.actualizar(id, dto));
    }

    // Eliminar clase
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        claseService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}