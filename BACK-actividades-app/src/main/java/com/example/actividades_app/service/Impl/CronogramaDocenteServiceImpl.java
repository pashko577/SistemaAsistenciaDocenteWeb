package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.AsignacionDocente;
import com.example.actividades_app.model.Entity.CronogramaDocente;
import com.example.actividades_app.model.Entity.HorarioBloque;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteResponseDTO;
import com.example.actividades_app.repository.AsignacionDocenteRepository;
import com.example.actividades_app.repository.CronogramaDocenteRepository;
import com.example.actividades_app.repository.HorarioBloqueRepository;
import com.example.actividades_app.service.CronogramaDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CronogramaDocenteServiceImpl implements CronogramaDocenteService {

    private final CronogramaDocenteRepository cronogramaRepository;
    private final AsignacionDocenteRepository asignacionRepository;
    private final HorarioBloqueRepository horarioBloqueRepository;

    @Override
    public CronogramaDocenteResponseDTO crear(CronogramaDocenteRequestDTO dto) {

        AsignacionDocente asignacion = asignacionRepository.findById(dto.getAsignacionDocenteId())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        HorarioBloque bloque = horarioBloqueRepository.findById(dto.getHorarioBloqueId())
                .orElseThrow(() -> new RuntimeException("Bloque horario no encontrado"));

        CronogramaDocente cronograma = CronogramaDocente.builder()
                .asignacionDocente(asignacion)
                .horarioBloque(bloque)
                .diaSemana(dto.getDiaSemana())
                .build();

        cronogramaRepository.save(cronograma);

        return mapToResponse(cronograma);
    }

    @Override
    public List<CronogramaDocenteResponseDTO> listar() {

        return cronogramaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CronogramaDocenteResponseDTO obtenerPorId(Long id) {

        CronogramaDocente cronograma = cronogramaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));

        return mapToResponse(cronograma);
    }

    @Override
    public void eliminar(Long id) {
        cronogramaRepository.deleteById(id);
    }

    private CronogramaDocenteResponseDTO mapToResponse(CronogramaDocente c) {

        return CronogramaDocenteResponseDTO.builder()
                .id(c.getId())
                .asignacionDocenteId(c.getAsignacionDocente().getId())
                .horarioBloqueId(c.getHorarioBloque().getId())
                .horaInicio(c.getHorarioBloque().getHoraInicio().toString())
                .horaFin(c.getHorarioBloque().getHoraFin().toString())
                .diaSemana(c.getDiaSemana())
                .build();
    }

}