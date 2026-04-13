package com.example.actividades_app.controller;

import java.util.List;

import org.hibernate.validator.constraints.ISBN;
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
import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoRequestDTO;
import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoResponseDTO;
import com.example.actividades_app.service.PeriodoAcademicoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/periodos")
@RequiredArgsConstructor
public class PeriodoAcademicoController {

    private final PeriodoAcademicoService periodoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeriodoAcademicoResponseDTO> crear(@Valid @RequestBody PeriodoAcademicoRequestDTO dto) {
        PeriodoAcademicoResponseDTO response = periodoService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<PeriodoAcademicoResponseDTO>> listar() {
        return ResponseEntity.ok(periodoService.listar());
    }

    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<PeriodoAcademicoResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(periodoService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PeriodoAcademicoResponseDTO> actualizar(@PathVariable Long id,
                                                                  @Valid @RequestBody PeriodoAcademicoRequestDTO dto) {
        return ResponseEntity.ok(periodoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        periodoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}