package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.Entity.ReporteDocente;
import com.example.actividades_app.model.Entity.TipoReporte;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteResponseDTO;
import com.example.actividades_app.repository.CronogramaDiarioRepository;
import com.example.actividades_app.repository.ReporteDocenteRepository;
import com.example.actividades_app.repository.TipoReporteRepository;
import com.example.actividades_app.service.ReporteDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteDocenteServiceImpl implements ReporteDocenteService {

    private final ReporteDocenteRepository reporteRepository;
    private final CronogramaDiarioRepository cronogramaDiarioRepository;
    private final TipoReporteRepository tipoReporteRepository;

    @Override
    public ReporteDocenteResponseDTO registrar(ReporteDocenteRequestDTO dto) {

        if (reporteRepository.existsByCronogramaDiarioId(dto.getCronogramaDiarioId())) {
            throw new RuntimeException("Ya existe un reporte para esta clase");
        }

        CronogramaDiario cronograma = cronogramaDiarioRepository.findById(dto.getCronogramaDiarioId())
                .orElseThrow(() -> new RuntimeException("Cronograma diario no encontrado"));

        TipoReporte tipoReporte = tipoReporteRepository.findById(dto.getTipoReporteId())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        ReporteDocente reporte = ReporteDocente.builder()
                .observaciones(dto.getObservaciones())
                .tardanza(dto.getTardanza())
                .cronogramaDiario(cronograma)
                .tipoReporte(tipoReporte)
                .build();

        reporteRepository.save(reporte);

        return mapToResponse(reporte);
    }

    @Override
    public List<ReporteDocenteResponseDTO> listar() {

        return reporteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ReporteDocenteResponseDTO obtenerPorId(Long id) {

        ReporteDocente reporte = reporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));

        return mapToResponse(reporte);
    }

    private ReporteDocenteResponseDTO mapToResponse(ReporteDocente r) {

        return ReporteDocenteResponseDTO.builder()
                .id(r.getId())
                .observaciones(r.getObservaciones())
                .tardanza(r.getTardanza())
                .cronogramaDiarioId(r.getCronogramaDiario().getId())
                .tipoReporteId(r.getTipoReporte().getId())
                .tipoReporteNombre(r.getTipoReporte().getNombreTipoReporte())
                .build();
    }

}