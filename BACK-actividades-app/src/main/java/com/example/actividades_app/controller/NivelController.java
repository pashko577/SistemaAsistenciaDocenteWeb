package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloCurso.NivelRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.NivelResponseDTO;
import com.example.actividades_app.service.NivelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/niveles")
@RequiredArgsConstructor
public class NivelController {

    private final NivelService nivelService;

    @PostMapping
    public ResponseEntity<NivelResponseDTO> crear(@Valid @RequestBody NivelRequestDTO dto) {
        NivelResponseDTO response = nivelService.crear(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<NivelResponseDTO>> listar() {
        return ResponseEntity.ok(nivelService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NivelResponseDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(nivelService.obtenerPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NivelResponseDTO> actualizar(@PathVariable Long id,
                                                       @Valid @RequestBody NivelRequestDTO dto) {
        return ResponseEntity.ok(nivelService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        nivelService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}