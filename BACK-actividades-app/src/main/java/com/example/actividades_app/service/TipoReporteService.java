package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.Reporte.TipoReporteRequestDTO;
import com.example.actividades_app.model.dto.Reporte.TipoReporteResponseDTO;

public interface TipoReporteService {

    TipoReporteResponseDTO crear(TipoReporteRequestDTO dto);

    List<TipoReporteResponseDTO> listar();

    TipoReporteResponseDTO obtenerPorId(Long id);

    void eliminar(Long id);

}