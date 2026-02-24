package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;
import com.example.actividades_app.service.CargoAdministrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/CargoAdministrativos")
@RequiredArgsConstructor
public class CargoAdministrativoController {

    private final CargoAdministrativoService cargoAdministrativoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CargoAdministrativo> crearCargoAdministrativo(@Valid @RequestBody CargoAdministrativoRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(cargoAdministrativoService.crearCargoAdministrativo(dto));
    }
    
}
