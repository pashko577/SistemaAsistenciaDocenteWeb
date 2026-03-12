package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioResponseDTO;

public interface CronogramaDiarioService {

    CronogramaDiarioResponseDTO crear(CronogramaDiarioRequestDTO dto);

    List<CronogramaDiarioResponseDTO> listar();

}