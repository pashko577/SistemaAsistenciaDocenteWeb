package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.dto.ModuloUsuario.SedeRequestDTO;
import com.example.actividades_app.service.SedeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor 
public class SedeController {
    
    private final SedeService sedeService;
    
    @PostMapping
    public ResponseEntity<Sede> crearSede(@Valid @RequestBody SedeRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(sedeService.crearSede(dto));
    }
}