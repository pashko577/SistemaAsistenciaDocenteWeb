package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoResponseDTO;

public interface TipoReemplazoService {

    TipoReemplazoResponseDTO crear(TipoReemplazoRequestDTO dto);

    List<TipoReemplazoResponseDTO> listar();

}