package com.example.actividades_app.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;
import com.example.actividades_app.service.TipoActividadService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/tipo-actividad")
@RequiredArgsConstructor
public class TipoActividadController {

    private final TipoActividadService tipoActividadService;

    @PostMapping
    public ResponseEntity<TipoActividadResponseDTO> crear(@Valid @RequestBody TipoActividadRequestDTO dto) {
        TipoActividadResponseDTO response = tipoActividadService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TipoActividadResponseDTO>> listar() {
        return ResponseEntity.ok(tipoActividadService.listar());
    }
}
