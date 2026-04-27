package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteResponseDTO;

public interface AsignacionDocenteService {

    AsignacionDocenteResponseDTO registrar(AsignacionDocenteRequestDTO dto);

    AsignacionDocenteResponseDTO obtenerPorId(Long id);

    List<AsignacionDocenteResponseDTO> listar();

    AsignacionDocenteResponseDTO actualizar(Long id, AsignacionDocenteRequestDTO dto);

    void eliminar(Long id);
}