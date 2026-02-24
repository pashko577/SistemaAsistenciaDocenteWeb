package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
import com.example.actividades_app.service.EspecialidadDocenteService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/especialidadDocentes")
@RequiredArgsConstructor
public class EspecialidadDocenteController {

    private final EspecialidadDocenteService especialidadDocenteService;

    @PostMapping
    public ResponseEntity<EspecialidadDocente> crearEspecialidadDocente(@Valid @RequestBody EspecialidadDocenteRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(especialidadDocenteService.crearEspecialidadDocente(dto));
    }
    
}
