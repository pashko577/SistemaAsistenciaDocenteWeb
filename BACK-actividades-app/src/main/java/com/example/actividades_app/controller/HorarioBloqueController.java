package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueRequestDTO;
import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueResponseDTO;
import com.example.actividades_app.service.HorarioBloqueService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/horarios-bloque")
@RequiredArgsConstructor
public class HorarioBloqueController {

    private final HorarioBloqueService horarioService;

    @PostMapping
    public ResponseEntity<HorarioBloqueResponseDTO> crear(
            @Valid @RequestBody HorarioBloqueRequestDTO dto) {
        HorarioBloqueResponseDTO response = horarioService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HorarioBloqueResponseDTO>> listar() {
        return ResponseEntity.ok(horarioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioBloqueResponseDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(horarioService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioBloqueResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HorarioBloqueRequestDTO dto) {
        return ResponseEntity.ok(horarioService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        horarioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}