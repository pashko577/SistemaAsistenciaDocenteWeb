package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteResponseDTO;
import com.example.actividades_app.service.ReemplazoDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reemplazos-docente")
@RequiredArgsConstructor
public class ReemplazoDocenteController {

    private final ReemplazoDocenteService reemplazoService;

    @PostMapping
    public ResponseEntity<ReemplazoDocenteResponseDTO> registrar(
            @Valid @RequestBody ReemplazoDocenteRequestDTO dto) {
        ReemplazoDocenteResponseDTO response = reemplazoService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ReemplazoDocenteResponseDTO>> listar() {
        return ResponseEntity.ok(reemplazoService.listar());
    }
}