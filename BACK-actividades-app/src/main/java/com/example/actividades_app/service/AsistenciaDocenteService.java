package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteResponseDTO;

public interface AsistenciaDocenteService {

    AsistenciaDocenteResponseDTO registrar(AsistenciaDocenteRequestDTO dto);

    List<AsistenciaDocenteResponseDTO> listar();

}