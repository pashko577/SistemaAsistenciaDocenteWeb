package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.Permisos.RolModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.RolModuloResponseDTO;
import com.example.actividades_app.service.RolModuloService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/rol-modulos")
@RequiredArgsConstructor
public class RolModuloController {

    private final RolModuloService rolModuloService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RolModuloResponseDTO> asignarModulo(
            @Valid @RequestBody RolModuloRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(rolModuloService.asignarModulo(dto));
    }

    @GetMapping("/rol/{rolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RolModuloResponseDTO>> listarPorRol(
            @PathVariable Long rolId) {

        return ResponseEntity.ok(rolModuloService.listarPorRol(rolId));
    }

    @DeleteMapping("/rol/{rolId}/modulo/{moduloId}")
    @PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<Void> desasignarModulo(
        @PathVariable Long rolId, 
        @PathVariable Long moduloId) {
    
    rolModuloService.desasignarModulo(rolId, moduloId);
    
    // Devolvemos 204 No Content porque la eliminación fue exitosa y no hay cuerpo que retornar
    return ResponseEntity.noContent().build();
}
}   