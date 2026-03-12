package com.example.actividades_app.service;

import com.example.actividades_app.model.dto.ModuloDocente.ClaseRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ClaseResponseDTO;

import java.util.List;

public interface ClaseService {

    ClaseResponseDTO crear(ClaseRequestDTO dto);

    List<ClaseResponseDTO> listar();

    ClaseResponseDTO obtenerPorId(Long id);

    ClaseResponseDTO actualizar(Long id, ClaseRequestDTO dto);

    void eliminar(Long id);
}