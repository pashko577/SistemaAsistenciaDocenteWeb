package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.Permisos.RolModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.RolModuloResponseDTO;

public interface RolModuloService {

    RolModuloResponseDTO asignarModulo(RolModuloRequestDTO dto);

    List<RolModuloResponseDTO> listarPorRol(Long rolId);

    void desasignarModulo(Long rolId, Long moduloId);
    
}