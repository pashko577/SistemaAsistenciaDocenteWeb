package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.Contrato.ContratoRequestDTO;
import com.example.actividades_app.model.dto.Contrato.ContratoResponseDTO;


public interface ContratoService {

    ContratoResponseDTO crear(ContratoRequestDTO dto);

    ContratoResponseDTO actualizar(Long id, ContratoRequestDTO dto);

    void eliminar(Long id);

    ContratoResponseDTO buscarPorId(Long id);

    List<ContratoResponseDTO> listar();

    ContratoResponseDTO buscarPorUsuario(Long usuarioId);
}
