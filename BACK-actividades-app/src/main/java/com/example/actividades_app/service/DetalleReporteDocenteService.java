package com.example.actividades_app.service;

import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteResponseDTO;

public interface DetalleReporteDocenteService {

    DetalleReporteDocenteResponseDTO registrar(DetalleReporteDocenteRequestDTO dto);

    DetalleReporteDocenteResponseDTO obtenerPorReporte(Long reporteId);

}