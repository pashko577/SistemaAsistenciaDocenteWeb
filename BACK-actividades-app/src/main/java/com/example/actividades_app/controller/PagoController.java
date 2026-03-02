package com.example.actividades_app.controller;

import com.example.actividades_app.model.dto.Pago.PagoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;
import com.example.actividades_app.service.PagoService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    
    // CREAR PAGO
   
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crear(
            @RequestBody PagoRequestDTO dto) {

        return ResponseEntity.ok(pagoService.crear(dto));
    }

   
    // ACTUALIZAR
   
    @PutMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id,
            @RequestBody PagoRequestDTO dto) {

        return ResponseEntity.ok(pagoService.actualizar(id, dto));
    }

  
    // ELIMINAR

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

   
    // BUSCAR POR ID
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(pagoService.buscarPorId(id));
    }

    // LISTAR POR USUARIO
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PagoResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                pagoService.listarPorUsuario(usuarioId));
    }

    // LISTAR POR FECHA
    @GetMapping("/fecha")
    public ResponseEntity<List<PagoResponseDTO>> listarPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        return ResponseEntity.ok(
                pagoService.listarPorFecha(fecha));
    }

    // LISTAR POR RANGO
    @GetMapping("/rango")
    public ResponseEntity<List<PagoResponseDTO>> listarPorRangoFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {

        return ResponseEntity.ok(
                pagoService.listarPorRangoFechas(inicio, fin));
    }

    // LISTAR POR USUARIO + RANGO
    @GetMapping("/usuario/rango")
    public ResponseEntity<List<PagoResponseDTO>> listarPorUsuarioYRango(
            @RequestParam Long usuarioId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {

        return ResponseEntity.ok(
                pagoService.listarPorUsuarioYRangoFechas(
                        usuarioId, inicio, fin));
    }

    // ÚLTIMO PAGO
    @GetMapping("/usuario/{usuarioId}/ultimo")
    public ResponseEntity<PagoResponseDTO> obtenerUltimoPago(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                pagoService.obtenerUltimoPago(usuarioId));
    }

    // RESUMEN GENERAL (BOLETA)
    @GetMapping("/{pagoId}/resumen")
    public ResponseEntity<ResumenGeneralResponseDTO> obtenerResumenPago(
            @PathVariable Long pagoId) {

        return ResponseEntity.ok(
                pagoService.obtenerResumenPago(pagoId));
    }
}