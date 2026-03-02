package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.dto.Pago.PagoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;

public interface PagoService {

    // =========================
    // CRUD
    // =========================
    PagoResponseDTO crear(PagoRequestDTO dto);

    PagoResponseDTO actualizar(Long id, PagoRequestDTO dto);

    void eliminar(Long id);

    PagoResponseDTO buscarPorId(Long id);

    // =========================
    // CONSULTAS
    // =========================
    List<PagoResponseDTO> listarPorUsuario(Long usuarioId);

    List<PagoResponseDTO> listarPorFecha(LocalDate fecha);

    List<PagoResponseDTO> listarPorRangoFechas(
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    List<PagoResponseDTO> listarPorUsuarioYRangoFechas(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    PagoResponseDTO obtenerUltimoPago(Long usuarioId);

    // =========================
    // EXTRA (PRO)
    // =========================
    ResumenGeneralResponseDTO obtenerResumenPago(Long pagoId);

}