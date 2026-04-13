package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoResponseDTO;
import com.example.actividades_app.service.AdministrativoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/administrativos")
@RequiredArgsConstructor
public class AdministrativoController {

    private final AdministrativoService administrativoService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdministrativoResponseDTO> registrarAdministrativo(
            @Valid @RequestBody AdministrativoRequestDTO dto) {

        AdministrativoResponseDTO response = administrativoService.registrarAdministrativo(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

  @GetMapping
    @IsStaff
    public ResponseEntity<List<AdministrativoResponseDTO>> listarAdministrativos() {
        return ResponseEntity.ok(administrativoService.listarAdministrativos());
    }

    // 3. FILTROS: También permitimos al ADMINISTRATIVO
    @GetMapping("/con-contrato")
    @IsStaff
    public ResponseEntity<List<AdministrativoResponseDTO>> listarConContrato() {
        return ResponseEntity.ok(administrativoService.listarSoloConContrato());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdministrativoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody AdministrativoRequestDTO request) {
        return ResponseEntity.ok(administrativoService.actualizarAdministrativo(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        administrativoService.eliminarAdministrativo(id);
        return ResponseEntity.noContent().build();
    }
}
