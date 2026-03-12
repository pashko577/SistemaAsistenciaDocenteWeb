package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloCurso.CursoRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.CursoResponseDTO;

public interface CursoService {

    CursoResponseDTO crear(CursoRequestDTO dto);

    List<CursoResponseDTO> listar();

    CursoResponseDTO obtenerPorId(Long id);

    CursoResponseDTO actualizar(Long id, CursoRequestDTO dto);

    void eliminar(Long id);

}