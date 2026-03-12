package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.Permisos.ModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.ModuloResponseDTO;

public interface ModuloService {

    ModuloResponseDTO crear(ModuloRequestDTO dto);

    List<ModuloResponseDTO> listar();

    ModuloResponseDTO obtenerPorId(Long id);

    void eliminar(Long id);
}