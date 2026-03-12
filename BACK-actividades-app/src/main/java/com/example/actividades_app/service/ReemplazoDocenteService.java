package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.ReemplazoDocenteResponseDTO;

public interface ReemplazoDocenteService {

    ReemplazoDocenteResponseDTO registrar(ReemplazoDocenteRequestDTO dto);

    List<ReemplazoDocenteResponseDTO> listar();

}