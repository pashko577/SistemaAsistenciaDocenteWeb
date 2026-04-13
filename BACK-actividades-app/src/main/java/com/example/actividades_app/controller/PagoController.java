package com.example.actividades_app.controller;

import com.example.actividades_app.config.IsStaff;
import com.example.actividades_app.model.dto.Pago.PagoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;
import com.example.actividades_app.service.PagoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
  
@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    // =========================
    // CREAR
    // =========================
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagoResponseDTO> crear(
            @Valid @RequestBody PagoRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pagoService.crear(dto));
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PagoRequestDTO dto) {

        return ResponseEntity.ok(
                pagoService.actualizar(id, dto));
    }

    // =========================
    // ELIMINAR
    // =========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        pagoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @GetMapping("/{id}")
    @IsStaff
    public ResponseEntity<PagoResponseDTO> buscarPorId(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                pagoService.buscarPorId(id));
    }

    // =========================
    // LISTAR POR USUARIO
    // =========================
    @GetMapping("/usuario/{usuarioId}")
    @IsStaff
    public ResponseEntity<List<PagoResponseDTO>> listarPorUsuario(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                pagoService.listarPorUsuario(usuarioId));
    }

    // =========================
    // LISTAR POR FECHA
    // =========================
    @GetMapping("/fecha")
    @IsStaff
    public ResponseEntity<List<PagoResponseDTO>> listarPorFecha(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {

        return ResponseEntity.ok(
                pagoService.listarPorFecha(fecha));
    }

    // =========================
    // LISTAR POR RANGO
    // =========================
    @GetMapping("/rango")
    @IsStaff
    public ResponseEntity<List<PagoResponseDTO>> listarPorRangoFechas(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate inicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fin) {


                if (inicio.isAfter(fin)) {
    throw new RuntimeException("La fecha inicio no puede ser mayor a la fecha fin");
}
        return ResponseEntity.ok(
                pagoService.listarPorRangoFechas(inicio, fin));
    }

    // =========================
    // USUARIO + RANGO
    // =========================
    @GetMapping("/usuario/{usuarioId}/rango")
    @IsStaff
    public ResponseEntity<List<PagoResponseDTO>> listarPorUsuarioYRango(
            @PathVariable Long usuarioId,

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

    // =========================
    // ÚLTIMO PAGO
    // =========================
    @GetMapping("/usuario/{usuarioId}/ultimo")
    @IsStaff
    public ResponseEntity<PagoResponseDTO> obtenerUltimoPago(
            @PathVariable Long usuarioId) {

        return ResponseEntity.ok(
                pagoService.obtenerUltimoPago(usuarioId));
    }

    // =========================
    // RESUMEN (BOLETA)
    // =========================
    @GetMapping("/{pagoId}/resumen")
    @IsStaff
    public ResponseEntity<ResumenGeneralResponseDTO> obtenerResumenPago(
            @PathVariable Long pagoId) {

        return ResponseEntity.ok(
                pagoService.obtenerResumenPago(pagoId));
    }
}