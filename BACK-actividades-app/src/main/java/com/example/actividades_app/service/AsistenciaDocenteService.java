package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDiariaDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteResponseDTO;

public interface AsistenciaDocenteService {

    AsistenciaDocenteResponseDTO registrar(AsistenciaDocenteRequestDTO dto);

    List<AsistenciaDocenteResponseDTO> listar();

    List<AsistenciaDiariaDTO> listarControlDiario(LocalDate fecha);

}