package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteResponseDTO;

public interface CronogramaDocenteService {

    CronogramaDocenteResponseDTO crear(CronogramaDocenteRequestDTO dto);

    List<CronogramaDocenteResponseDTO> listar();

    CronogramaDocenteResponseDTO obtenerPorId(Long id);

    void eliminar(Long id);

}