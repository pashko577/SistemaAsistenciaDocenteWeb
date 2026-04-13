package com.example.actividades_app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoResponseDTO;
import com.example.actividades_app.service.TipoReemplazoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tipos-reemplazo")
@RequiredArgsConstructor
public class TipoReemplazoController {

    private final TipoReemplazoService tipoService;

    @PostMapping
        @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TipoReemplazoResponseDTO> crear(
            @Valid @RequestBody TipoReemplazoRequestDTO dto) {
        TipoReemplazoResponseDTO response = tipoService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @IsStaff
    public ResponseEntity<List<TipoReemplazoResponseDTO>> listar() {
        return ResponseEntity.ok(tipoService.listar());
    }
}