package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.Entity.ReporteDocente;
import com.example.actividades_app.model.Entity.TipoReporte;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ReporteDocenteResponseDTO;
import com.example.actividades_app.repository.AsistenciaDocenteRepository;
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
    private final AsistenciaDocenteRepository asistenciaRepository; // <--- Inyectar esto

    @Override
    public ReporteDocenteResponseDTO registrar(ReporteDocenteRequestDTO dto) {

        if (reporteRepository.existsByCronogramaDiarioId(dto.getCronogramaDiarioId())) {
            throw new RuntimeException("Ya existe un reporte para esta clase");
        }

        CronogramaDiario cronograma = cronogramaDiarioRepository.findById(dto.getCronogramaDiarioId())
                .orElseThrow(() -> new RuntimeException("Cronograma diario no encontrado"));

        TipoReporte tipoReporte = tipoReporteRepository.findById(dto.getTipoReporteId())
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        // Ya no seteamos ni fecha ni tardanza, el objeto es más ligero
        ReporteDocente reporte = ReporteDocente.builder()
                .observaciones(dto.getObservaciones())
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
        // Buscamos la tardanza en la tabla de asistencia vinculada al mismo cronograma
        Integer minutosTardanza = asistenciaRepository.findByCronogramaDiarioId(r.getCronogramaDiario().getId())
                .map(asistencia -> asistencia.getMinutosTardanza())
                .orElse(0);

        return ReporteDocenteResponseDTO.builder()
                .id(r.getId())
                // JALAMOS LA FECHA DEL CRONOGRAMA
                .fecha(r.getCronogramaDiario().getFecha()) 
                .observaciones(r.getObservaciones())
                // JALAMOS LA TARDANZA DE LA ASISTENCIA
                .tardanza(minutosTardanza) 
                .cronogramaDiarioId(r.getCronogramaDiario().getId())
                .tipoReporteId(r.getTipoReporte().getId())
                .tipoReporteNombre(r.getTipoReporte().getNombreTipoReporte())
                .build();
    }
}