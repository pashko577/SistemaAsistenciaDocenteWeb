package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.Docente;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.service.DocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/Docentes")
@RequiredArgsConstructor
public class DocenteController {
    private final DocenteService docenteService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Docente> registrarDocente(@Valid @RequestBody DocenteRequestDTO dto){
        docenteService.registerDocente(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }    
}
