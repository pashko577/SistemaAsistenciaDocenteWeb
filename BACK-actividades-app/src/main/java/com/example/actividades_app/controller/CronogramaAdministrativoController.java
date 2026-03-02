package com.example.actividades_app.controller;


import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;
import com.example.actividades_app.service.CronogramaAdministrativoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/cronograma-administrativo")
@RequiredArgsConstructor
public class CronogramaAdministrativoController {

    private final CronogramaAdministrativoService service;

    // Cronograma
    @PostMapping
    public ResponseEntity<CronogramaAdministrativoResponseDTO> crear(
            @RequestBody CronogramaAdministrativoRequestDTO dto) {

        CronogramaAdministrativoResponseDTO response = service.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Actualizar
    @PutMapping("/{id}")
    public ResponseEntity<CronogramaAdministrativoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody CronogramaAdministrativoRequestDTO dto) {

        CronogramaAdministrativoResponseDTO response = service.actualizar(id, dto);
        return ResponseEntity.ok(response);
    }

    // Eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // buscar por id
    @GetMapping("/{id}")
    public ResponseEntity<CronogramaAdministrativoResponseDTO> buscarPorId(@PathVariable Long id) {
        CronogramaAdministrativoResponseDTO response = service.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

   
    // LISTAR CRONOGRAMAS POR ADMINISTRATIVO
    
    @GetMapping("/administrativo/{administrativoId}")
    public ResponseEntity<List<CronogramaAdministrativoResponseDTO>> listarPorAdministrativo(
            @PathVariable Long administrativoId) {

        List<CronogramaAdministrativoResponseDTO> responseList = 
                service.listarPorAdministrativo(administrativoId);

        return ResponseEntity.ok(responseList);
    }

   
    // LISTAR CRONOGRAMAS POR FECHA
   
    @GetMapping("/fecha")
    public ResponseEntity<List<CronogramaAdministrativoResponseDTO>> listarPorFecha(
            @RequestParam LocalDate fecha) {

        List<CronogramaAdministrativoResponseDTO> responseList = service.listarPorFecha(fecha);
        return ResponseEntity.ok(responseList);
    }

    
    // BUSCAR CRONOGRAMA POR ADMINISTRATIVO + FECHA
 
    @GetMapping("/administrativo/{administrativoId}/fecha")
    public ResponseEntity<CronogramaAdministrativoResponseDTO> buscarPorAdministrativoYFecha(
            @PathVariable Long administrativoId,
            @RequestParam LocalDate fecha) {

        CronogramaAdministrativoResponseDTO response = 
                service.buscarPorAdministrativoYFecha(administrativoId, fecha);

        return ResponseEntity.ok(response);
    }
}


 