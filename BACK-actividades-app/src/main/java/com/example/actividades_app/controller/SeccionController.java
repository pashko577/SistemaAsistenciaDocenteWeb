package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloCurso.SeccionRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.SeccionResponseDTO;
import com.example.actividades_app.service.SeccionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/secciones")
@RequiredArgsConstructor
public class SeccionController {

    private final SeccionService seccionService;

    @PostMapping
        @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<SeccionResponseDTO> crear(@Valid @RequestBody SeccionRequestDTO dto) {
        SeccionResponseDTO response = seccionService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<SeccionResponseDTO>> listar() {
        return ResponseEntity.ok(seccionService.listar());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<SeccionResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(seccionService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<SeccionResponseDTO> actualizar(@PathVariable Long id,
                                                         @Valid @RequestBody SeccionRequestDTO dto) {
        return ResponseEntity.ok(seccionService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        seccionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}