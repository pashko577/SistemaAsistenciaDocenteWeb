package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.Permisos.ModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.ModuloResponseDTO;
import com.example.actividades_app.service.ModuloService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/modulos")
@RequiredArgsConstructor
public class ModuloController {

    private final ModuloService moduloService;

    @PostMapping
    public ResponseEntity<ModuloResponseDTO> crear(
            @Valid @RequestBody ModuloRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(moduloService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<ModuloResponseDTO>> listar() {

        return ResponseEntity.ok(moduloService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ModuloResponseDTO> obtener(@PathVariable Long id) {

        return ResponseEntity.ok(moduloService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        moduloService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}