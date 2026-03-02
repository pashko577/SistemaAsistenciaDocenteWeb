package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoResponseDTO;

public interface AsistenciaAdministrativoService {

    AsistenciaAdministrativoResponseDTO registrar(AsistenciaAdministrativoRequestDTO dto);

    AsistenciaAdministrativoResponseDTO actualizar(Long id, AsistenciaAdministrativoRequestDTO dto);

    void eliminar(Long id);

    AsistenciaAdministrativoResponseDTO buscarPorId(Long id);

    List<AsistenciaAdministrativoResponseDTO> listarPorAdministrativo(Long administrativoId);

    AsistenciaAdministrativoResponseDTO buscarPorAdministrativoYFecha(
            Long administrativoId,
            LocalDate fecha);
}
