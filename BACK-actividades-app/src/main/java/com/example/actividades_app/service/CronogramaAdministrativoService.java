package com.example.actividades_app.service;


import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;



public interface CronogramaAdministrativoService {

    CronogramaAdministrativoResponseDTO crear(CronogramaAdministrativoRequestDTO dto);

    CronogramaAdministrativoResponseDTO actualizar(Long id, CronogramaAdministrativoRequestDTO dto);

    void eliminar(Long id);

    CronogramaAdministrativoResponseDTO buscarPorId(Long id);

    List<CronogramaAdministrativoResponseDTO> listarPorAdministrativo(Long administrativoId);

    List<CronogramaAdministrativoResponseDTO> listarPorFecha(LocalDate fecha);

    CronogramaAdministrativoResponseDTO buscarPorAdministrativoYFecha(Long administrativoId, LocalDate fecha);
}