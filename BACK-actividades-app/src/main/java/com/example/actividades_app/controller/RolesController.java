package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.actividades_app.service.RolService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.dto.ModuloUsuario.RegistrarRolRequestDTO;

@RestController
@RequestMapping("/z")
public class RolesController {

    @Autowired
    private RolService rolService;

    @DeleteMapping("/EliminarRol/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> eliminarRol(@PathVariable Long id) {
        rolService.eliminarRol(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/RegistrarRol")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> crearRol(@Valid @RequestBody RegistrarRolRequestDTO dto) {
        Rol rolCreado = rolService.registrarRol(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(rolCreado);
    }
}
