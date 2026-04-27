package com.example.actividades_app.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDiariaDTO;
import com.example.actividades_app.service.AsistenciaDocenteService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/control-diario")
@RequiredArgsConstructor
public class ControlDiarioController {
    private final AsistenciaDocenteService asistenciaDocenteService;

    @GetMapping("/reporte")
    public ResponseEntity<List<AsistenciaDiariaDTO>> obtenerReporteDiario(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {

        List<AsistenciaDiariaDTO> lista = asistenciaDocenteService.listarControlDiario(fecha);

        if (lista.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(lista);
    }

}
