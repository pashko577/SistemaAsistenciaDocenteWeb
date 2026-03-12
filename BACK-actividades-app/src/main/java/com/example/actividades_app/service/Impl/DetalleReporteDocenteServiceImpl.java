package com.example.actividades_app.service.Impl;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.DetalleReporteDocente;
import com.example.actividades_app.model.Entity.ReporteDocente;
import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DetalleReporteDocenteResponseDTO;
import com.example.actividades_app.repository.DetalleReporteDocenteRepository;
import com.example.actividades_app.repository.ReporteDocenteRepository;
import com.example.actividades_app.service.DetalleReporteDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DetalleReporteDocenteServiceImpl implements DetalleReporteDocenteService {

    private final DetalleReporteDocenteRepository detalleRepository;
    private final ReporteDocenteRepository reporteRepository;

    @Override
    public DetalleReporteDocenteResponseDTO registrar(DetalleReporteDocenteRequestDTO dto) {

        ReporteDocente reporte = reporteRepository.findById(dto.getReporteDocenteId())
                .orElseThrow(() -> new RuntimeException("Reporte docente no encontrado"));

        DetalleReporteDocente detalle = DetalleReporteDocente.builder()
                .reporteDocente(reporte)
                .publiWSS(dto.getPubliWSS())
                .publiWFS(dto.getPubliWFS())
                .agendaE(dto.getAgendaE())
                .build();

        detalleRepository.save(detalle);

        return mapToResponse(detalle);
    }

    @Override
    public DetalleReporteDocenteResponseDTO obtenerPorReporte(Long reporteId) {

        DetalleReporteDocente detalle = detalleRepository.findByReporteDocenteId(reporteId)
                .orElseThrow(() -> new RuntimeException("Detalle no encontrado"));

        return mapToResponse(detalle);
    }

    private DetalleReporteDocenteResponseDTO mapToResponse(DetalleReporteDocente d) {

        return DetalleReporteDocenteResponseDTO.builder()
                .id(d.getId())
                .reporteDocenteId(d.getReporteDocente().getId())
                .publiWSS(d.getPubliWSS())
                .publiWFS(d.getPubliWFS())
                .agendaE(d.getAgendaE())
                .build();
    }

}