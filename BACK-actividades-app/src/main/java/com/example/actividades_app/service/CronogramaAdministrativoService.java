package com.example.actividades_app.service;


import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;



public interface CronogramaAdministrativoService {

    CronogramaAdministrativoResponseDTO crear(
            CronogramaAdministrativoRequestDTO dto);

    CronogramaAdministrativoResponseDTO actualizar(
            Long id,
            CronogramaAdministrativoRequestDTO dto);

    void eliminar(Long id);

    CronogramaAdministrativoResponseDTO buscarPorId(Long id);

    List<CronogramaAdministrativoResponseDTO>
            listarPorAdministrativo(Long administrativoId);

    // ✅ NUEVO MODELO SEMANAL
    List<CronogramaAdministrativoResponseDTO>
            listarPorDiaSemana(CronogramaAdministrativo.DiaSemana dia);

    CronogramaAdministrativoResponseDTO
            buscarPorAdministrativoYDiaSemana(
                    Long administrativoId,
                    CronogramaAdministrativo.DiaSemana dia);
}