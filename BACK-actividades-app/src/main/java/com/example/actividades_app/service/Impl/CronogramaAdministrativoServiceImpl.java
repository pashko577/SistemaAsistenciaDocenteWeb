package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;


import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.CronogramaAdministrativoResponseDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.CronogramaAdministrativoService;


@Service
@RequiredArgsConstructor
@Transactional
public class CronogramaAdministrativoServiceImpl
        implements CronogramaAdministrativoService {

    private final CronogramaAdministrativoRepository cronogramaRepository;
    private final AdministrativoRepository administrativoRepository;

    // ============================
    // CREAR
    // ============================
    @Override
    public CronogramaAdministrativoResponseDTO crear(
            CronogramaAdministrativoRequestDTO dto) {

        Administrativo admin = administrativoRepository
                .findById(dto.getAdministrativoId())
                .orElseThrow(() ->
                        new RuntimeException("Administrativo no encontrado"));

        CronogramaAdministrativo.DiaSemana dia =
        CronogramaAdministrativo.DiaSemana
                .valueOf(dto.getDiaSemana().toUpperCase());

        // validar duplicados
        if (cronogramaRepository
                .existsByAdministrativoIdAndDiaSemana(admin.getId(), dia)) {
            throw new RuntimeException(
                    "Ya existe cronograma para ese día");
        }

        CronogramaAdministrativo cronograma =
                CronogramaAdministrativo.builder()
                        .horaEntrada(dto.getHoraEntrada())
                        .horaSalida(dto.getHoraSalida())
                        .diaSemana(dia)
                        .administrativo(admin)
                        .build();

        cronogramaRepository.save(cronograma);

        return mapToDTO(cronograma);
    }

    // ============================
    // ACTUALIZAR
    // ============================
    @Override
    public CronogramaAdministrativoResponseDTO actualizar(
            Long id,
            CronogramaAdministrativoRequestDTO dto) {

        CronogramaAdministrativo cronograma =
                cronogramaRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Cronograma no encontrado"));

        cronograma.setHoraEntrada(dto.getHoraEntrada());
        cronograma.setHoraSalida(dto.getHoraSalida());
        cronograma.setDiaSemana(
                CronogramaAdministrativo.DiaSemana
                        .valueOf(dto.getDiaSemana().toUpperCase())
        );

        return mapToDTO(cronograma);
    }

    // ============================
    // ELIMINAR
    // ============================
    @Override
    public void eliminar(Long id) {
        cronogramaRepository.deleteById(id);
    }

    // ============================
    // BUSCAR POR ID
    // ============================
    @Override
    public CronogramaAdministrativoResponseDTO buscarPorId(Long id) {

        return cronogramaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() ->
                        new RuntimeException("Cronograma no encontrado"));
    }

    // ============================
    // LISTAR POR ADMINISTRATIVO
    // ============================
    @Override
    public List<CronogramaAdministrativoResponseDTO>
    listarPorAdministrativo(Long administrativoId) {

        return cronogramaRepository
                .findByAdministrativoId(administrativoId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ============================
    // LISTAR POR DIA SEMANA
    // ============================
    @Override
    public List<CronogramaAdministrativoResponseDTO>
    listarPorDiaSemana(CronogramaAdministrativo.DiaSemana dia) {

        return cronogramaRepository
                .findByDiaSemana(dia)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // ============================
    // BUSCAR ADMIN + DIA
    // ============================
    @Override
    public CronogramaAdministrativoResponseDTO
    buscarPorAdministrativoYDiaSemana(
            Long administrativoId,
            CronogramaAdministrativo.DiaSemana dia) {

        return cronogramaRepository
                .findByAdministrativoIdAndDiaSemana(administrativoId, dia)
                .map(this::mapToDTO)
                .orElseThrow(() ->
                        new RuntimeException("Cronograma no encontrado"));
    }

    // ============================
    // MAPPER
    // ============================
    private CronogramaAdministrativoResponseDTO mapToDTO(
            CronogramaAdministrativo entity) {

        return CronogramaAdministrativoResponseDTO.builder()
                .id(entity.getId())
                .horaEntrada(entity.getHoraEntrada())
                .horaSalida(entity.getHoraSalida())
                .diaSemana(entity.getDiaSemana().name())
                .administrativoId(entity.getAdministrativo().getId())
                .build();
    }
}