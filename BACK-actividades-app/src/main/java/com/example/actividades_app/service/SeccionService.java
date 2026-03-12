package com.example.actividades_app.service;


import java.util.List;

import com.example.actividades_app.model.dto.ModuloCurso.SeccionRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.SeccionResponseDTO;

public interface SeccionService {

    SeccionResponseDTO crear(SeccionRequestDTO dto);

    SeccionResponseDTO actualizar(Long id, SeccionRequestDTO dto);

    SeccionResponseDTO obtenerPorId(Long id);

    List<SeccionResponseDTO> listar();

    void eliminar(Long id);

}