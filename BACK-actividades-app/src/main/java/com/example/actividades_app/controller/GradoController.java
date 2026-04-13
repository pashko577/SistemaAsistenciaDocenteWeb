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
import com.example.actividades_app.model.dto.ModuloCurso.GradoRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.GradoResponseDTO;
import com.example.actividades_app.service.GradoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/grados")
@RequiredArgsConstructor
public class GradoController {

    private final GradoService gradoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradoResponseDTO> crear(@Valid @RequestBody GradoRequestDTO dto) {
        GradoResponseDTO response = gradoService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<GradoResponseDTO>> listar() {
        return ResponseEntity.ok(gradoService.listar());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<GradoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(gradoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<GradoResponseDTO> actualizar(@PathVariable Long id,
            @Valid @RequestBody GradoRequestDTO dto) {
        return ResponseEntity.ok(gradoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")

    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        gradoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}