package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.PeriodoAcademico;
import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoRequestDTO;
import com.example.actividades_app.model.dto.ClaseDocente.PeriodoAcademicoResponseDTO;
import com.example.actividades_app.repository.PeriodoAcademicoRepository;
import com.example.actividades_app.service.PeriodoAcademicoService;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class PeriodoAcademicoServiceImpl implements PeriodoAcademicoService {

    private final PeriodoAcademicoRepository periodoRepository;

    @Override
    public PeriodoAcademicoResponseDTO crear(PeriodoAcademicoRequestDTO dto) {

        if (periodoRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("El periodo académico ya existe");
        }

        PeriodoAcademico periodo = PeriodoAcademico.builder()
                .nombre(dto.getNombre())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .estado(PeriodoAcademico.EstadoPeriodo.ACTIVO) // Estado por defecto al crear
                .build();

        periodoRepository.save(periodo);

        return mapToResponse(periodo);
    }

    @Override
    public List<PeriodoAcademicoResponseDTO> listar() {
        return periodoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PeriodoAcademicoResponseDTO obtenerPorId(Long id) {
        PeriodoAcademico periodo = periodoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Periodo académico no encontrado"));

        return mapToResponse(periodo);
    }

    @Override
    public PeriodoAcademicoResponseDTO actualizar(Long id, PeriodoAcademicoRequestDTO dto) {
        PeriodoAcademico periodo = periodoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Periodo académico no encontrado"));

        if (!periodo.getNombre().equals(dto.getNombre()) && periodoRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("El nombre del periodo académico ya existe");
        }

        periodo.setNombre(dto.getNombre());
        periodo.setFechaInicio(dto.getFechaInicio());
        periodo.setFechaFin(dto.getFechaFin());

        // Si tu DTO tuviera estado, se podría actualizar aquí
        // periodo.setEstado(dto.getEstado());

        periodoRepository.save(periodo);

        return mapToResponse(periodo);
    }

    @Override
    public void eliminar(Long id) {
        if (!periodoRepository.existsById(id)) {
            throw new RuntimeException("Periodo académico no encontrado");
        }
        periodoRepository.deleteById(id);
    }

    private PeriodoAcademicoResponseDTO mapToResponse(PeriodoAcademico p) {
        return PeriodoAcademicoResponseDTO.builder()
                .id(p.getId())
                .nombre(p.getNombre())
                .fechaInicio(p.getFechaInicio())
                .fechaFin(p.getFechaFin())
                .build();
    }
}