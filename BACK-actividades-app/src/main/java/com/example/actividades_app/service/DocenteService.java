package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteResponseDTO;

public interface DocenteService {
    // Retornar el DTO permite confirmar al front qué se guardó exactamente
    DocenteResponseDTO registerDocente(DocenteRequestDTO request);
    
    List<DocenteResponseDTO> listarTodos();

    List<DocenteResponseDTO> listarDocentesConContrato();
    
    // Al actualizar, devuelves la nueva versión del docente
    DocenteResponseDTO actualizarDocente(Long id, DocenteRequestDTO request);
    
    // Al cambiar estado, devuelves el objeto para ver el nuevo badge (ACTIVO/INACTIVO)
void eliminarDocente(Long id);

}