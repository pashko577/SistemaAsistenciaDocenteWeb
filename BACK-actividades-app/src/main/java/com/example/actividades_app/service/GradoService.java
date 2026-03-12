package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloCurso.GradoRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.GradoResponseDTO;

public interface GradoService {
GradoResponseDTO crear(GradoRequestDTO dto);

GradoResponseDTO actualizar (Long id, GradoRequestDTO dto);

GradoResponseDTO obtenerPorId(Long id);

List<GradoResponseDTO> listar();

void  eliminar(Long id
);


}
