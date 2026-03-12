package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloCurso.NivelRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.NivelResponseDTO;

public interface NivelService {
    NivelResponseDTO crear(NivelRequestDTO dto);

    NivelResponseDTO actualizar(Long id, NivelRequestDTO dto);

    NivelResponseDTO obtenerPorId(Long id);

    List<NivelResponseDTO> listar();

    void eliminar(Long id);

    
}