package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoRequestDTO;
import com.example.actividades_app.service.AdministrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/Administrativos")
@RequiredArgsConstructor
public class AdministrativoController {

    private final AdministrativoService administrativoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Administrativo> registrarAdministrativo(@Valid @RequestBody AdministrativoRequestDTO dto){
        administrativoService.registrarAdministrativo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }   
}
