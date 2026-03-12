package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.Entity.CronogramaDocente;
import com.example.actividades_app.model.Entity.CronogramaDiario.EstadoClase;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDiarioResponseDTO;
import com.example.actividades_app.repository.CronogramaDiarioRepository;
import com.example.actividades_app.repository.CronogramaDocenteRepository;
import com.example.actividades_app.service.CronogramaDiarioService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CronogramaDiarioServiceImpl implements CronogramaDiarioService {

    private final CronogramaDiarioRepository cronogramaDiarioRepository;
    private final CronogramaDocenteRepository cronogramaDocenteRepository;

    @Override
    public CronogramaDiarioResponseDTO crear(CronogramaDiarioRequestDTO dto) {

        CronogramaDocente cronograma = cronogramaDocenteRepository.findById(dto.getCronogramaDocenteId())
                .orElseThrow(() -> new RuntimeException("Cronograma docente no encontrado"));

        if (cronogramaDiarioRepository.existsByCronogramaDocenteIdAndFecha(
                dto.getCronogramaDocenteId(), dto.getFecha())) {

            throw new RuntimeException("Ya existe una clase programada ese día");
        }

        CronogramaDiario diario = CronogramaDiario.builder()
                .cronogramaDocente(cronograma)
                .fecha(dto.getFecha())
                .estadoClase(EstadoClase.PROGRAMADA)
                .build();

        cronogramaDiarioRepository.save(diario);

        return mapToResponse(diario);
    }

    @Override
    public List<CronogramaDiarioResponseDTO> listar() {

        return cronogramaDiarioRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CronogramaDiarioResponseDTO mapToResponse(CronogramaDiario d) {

        return CronogramaDiarioResponseDTO.builder()
                .id(d.getId())
                .fecha(d.getFecha())
                .estadoClase(d.getEstadoClase())
                .cronogramaDocenteId(d.getCronogramaDocente().getId())
                .build();
    }

}