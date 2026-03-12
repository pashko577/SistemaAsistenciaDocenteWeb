package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueRequestDTO;
import com.example.actividades_app.model.dto.ModuloHorario.HorarioBloqueResponseDTO;

public interface HorarioBloqueService {

    HorarioBloqueResponseDTO crear(HorarioBloqueRequestDTO dto);

    List<HorarioBloqueResponseDTO> listar();

    HorarioBloqueResponseDTO obtenerPorId(Long id);

    HorarioBloqueResponseDTO actualizar(Long id, HorarioBloqueRequestDTO dto);

    void eliminar(Long id);

}