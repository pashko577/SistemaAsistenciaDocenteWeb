package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteResponseDTO;
import com.example.actividades_app.service.DocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/Docentes")
@RequiredArgsConstructor
public class DocenteController {
    private final DocenteService docenteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocenteResponseDTO> registrarDocente(@Valid @RequestBody DocenteRequestDTO dto) {
        // Ahora devuelve el DTO creado
        return ResponseEntity.status(HttpStatus.CREATED).body(docenteService.registerDocente(dto));
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<DocenteResponseDTO>> listarDocentes() {
        return ResponseEntity.ok(docenteService.listarTodos());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DocenteResponseDTO> actualizarDocente(@PathVariable Long id,
            @Valid @RequestBody DocenteRequestDTO dto) {
        return ResponseEntity.ok(docenteService.actualizarDocente(id, dto));
    }

    @DeleteMapping("/{id}") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarDocente(@PathVariable Long id) {
        docenteService.eliminarDocente(id);
        return ResponseEntity.noContent().build();
    }
}