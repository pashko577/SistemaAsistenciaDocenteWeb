package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteResponseDTO;

public interface ReporteDocenteService {

    ReporteDocenteResponseDTO registrar(ReporteDocenteRequestDTO dto);

    List<ReporteDocenteResponseDTO> listar();

    ReporteDocenteResponseDTO obtenerPorId(Long id);

}