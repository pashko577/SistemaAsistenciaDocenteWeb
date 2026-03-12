package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoRequestDTO;
import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoResponseDTO;

public interface PeriodoAcademicoService {
    PeriodoAcademicoResponseDTO crear(PeriodoAcademicoRequestDTO dto);

    List<PeriodoAcademicoResponseDTO> listar();

    PeriodoAcademicoResponseDTO obtenerPorId(Long id);

    PeriodoAcademicoResponseDTO actualizar(Long id, PeriodoAcademicoRequestDTO dto);

    void eliminar(Long id);
}
