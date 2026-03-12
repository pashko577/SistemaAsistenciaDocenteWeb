package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteResponseDTO;
import com.example.actividades_app.service.AsistenciaDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asistencias-docente")
@RequiredArgsConstructor
public class AsistenciaDocenteController {

    private final AsistenciaDocenteService asistenciaService;

    @PostMapping
    public ResponseEntity<AsistenciaDocenteResponseDTO> registrar(
            @Valid @RequestBody AsistenciaDocenteRequestDTO dto) {
        AsistenciaDocenteResponseDTO response = asistenciaService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<AsistenciaDocenteResponseDTO>> listar() {
        return ResponseEntity.ok(asistenciaService.listar());
    }
}