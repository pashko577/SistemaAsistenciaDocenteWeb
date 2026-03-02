package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
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
    public ResponseEntity<EspecialidadDocente> crearEspecialidadDocente(@Valid @RequestBody EspecialidadDocenteRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadDocenteService.crearEspecialidadDocente(dto));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<EspecialidadDocente>> listar() {
        return ResponseEntity.ok(especialidadDocenteService.listarEspecialidadDocente());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EspecialidadDocente> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(especialidadDocenteService.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        especialidadDocenteService.eliminarEspecilidadDocente(id);
        return ResponseEntity.noContent().build();
    }
    
}
