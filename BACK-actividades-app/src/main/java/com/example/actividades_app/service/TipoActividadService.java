package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.enums.TipoPlanilla;
import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;

public interface TipoActividadService {

    TipoActividadResponseDTO crear(TipoActividadRequestDTO dto);

    List<TipoActividadResponseDTO> listar();

    TipoActividadResponseDTO actualizar(Long id, TipoActividadRequestDTO dto);

    List<TipoActividadResponseDTO> listarPorPlanilla(TipoPlanilla planilla);

    TipoActividadResponseDTO buscarPorId(Long id);

    void eliminar(Long id);

}