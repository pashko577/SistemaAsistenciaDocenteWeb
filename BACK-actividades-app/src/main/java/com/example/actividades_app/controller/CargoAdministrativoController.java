package com.example.actividades_app.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CargoAdministrativoResponseDTO;
import com.example.actividades_app.service.CargoAdministrativoService;

import java.util.List;

@RestController
@RequestMapping("/api/cargo-administrativos")
@RequiredArgsConstructor
@Validated
public class CargoAdministrativoController {

    private final CargoAdministrativoService cargoAdministrativoService;

    // 1. LISTAR TODOS
    @GetMapping
    @IsStaff
    public ResponseEntity<List<CargoAdministrativoResponseDTO>> listarCargos() {
        return ResponseEntity.ok(cargoAdministrativoService.listarTodosCargos());
    }

    // 2. CREAR NUEVO
    @PostMapping
@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CargoAdministrativoResponseDTO> crearCargo(
            @Valid @RequestBody CargoAdministrativoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(cargoAdministrativoService.crearCargoAdministrativo(dto));
    }

    // 3. ACTUALIZAR POR ID
    @PutMapping("/{id}")
   @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CargoAdministrativoResponseDTO> actualizarCargo(
            @PathVariable("id") Long id,
            @RequestBody CargoAdministrativoRequestDTO dto) {
        // Usamos el nombreCargo que viene en el cuerpo del DTO
        return ResponseEntity.ok(cargoAdministrativoService.actualizarAdministrativo(id, dto.getNombreCargo()));
    }

    // 4. ELIMINAR POR ID
    @DeleteMapping("/{id}")
 @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminarCargo(@PathVariable("id") Long id) {
        cargoAdministrativoService.eliminarCargoAdministrativo(id);
        return ResponseEntity.noContent().build();
    }
}