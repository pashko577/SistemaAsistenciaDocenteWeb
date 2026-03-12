package com.example.actividades_app.service;



import java.util.List;

import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;


public interface TipoActividadService {

    TipoActividadResponseDTO crear(TipoActividadRequestDTO dto);

    List<TipoActividadResponseDTO> listar();

}