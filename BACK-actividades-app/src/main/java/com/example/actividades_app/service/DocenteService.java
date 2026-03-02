package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteResponseDTO;

public interface DocenteService {
    void registerDocente(DocenteRequestDTO request);
    List<DocenteResponseDTO> listarTodos();
}
