package com.example.actividades_app.service;

import java.util.List;


import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteResponseDTO;

public interface EspecialidadDocenteService {
    EspecialidadDocenteResponseDTO crearEspecialidadDocente(EspecialidadDocenteRequestDTO dto);
    
    EspecialidadDocenteResponseDTO actualizarEspecialidadDocente(Long id, String nombre);

    void eliminarEspecilidadDocente(Long id);

    EspecialidadDocenteResponseDTO obtenerPorId(Long id);

    List<EspecialidadDocenteResponseDTO> listarEspecialidadDocente();
}