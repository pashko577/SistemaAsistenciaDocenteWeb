package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteResponseDTO;
import com.example.actividades_app.service.EspecialidadDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/especialidadDocentes")
@RequiredArgsConstructor
public class EspecialidadDocenteController {

    private final EspecialidadDocenteService especialidadDocenteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadDocenteResponseDTO> crearEspecialidadDocente(@Valid @RequestBody EspecialidadDocenteRequestDTO dto){
        // El service ahora devuelve el DTO con el ID ya mapeado
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadDocenteService.crearEspecialidadDocente(dto));
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<EspecialidadDocenteResponseDTO>> listar() {
        // Devuelve la lista de DTOs limpia, sin recursión infinita
        return ResponseEntity.ok(especialidadDocenteService.listarEspecialidadDocente());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<EspecialidadDocenteResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadDocenteService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadDocenteService.eliminarEspecilidadDocente(id);
        return ResponseEntity.noContent().build();
    }
}