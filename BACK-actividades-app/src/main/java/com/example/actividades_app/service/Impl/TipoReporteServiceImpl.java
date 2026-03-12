package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.TipoReporte;
import com.example.actividades_app.model.dto.Reporte.TipoReporteRequestDTO;
import com.example.actividades_app.model.dto.Reporte.TipoReporteResponseDTO;
import com.example.actividades_app.repository.TipoReporteRepository;
import com.example.actividades_app.service.TipoReporteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoReporteServiceImpl implements TipoReporteService {

    private final TipoReporteRepository tipoReporteRepository;

    @Override
    public TipoReporteResponseDTO crear(TipoReporteRequestDTO dto) {

        if (tipoReporteRepository.existsByNombreTipoReporte(dto.getNombreTipoReporte())) {
            throw new RuntimeException("El tipo de reporte ya existe");
        }

        TipoReporte tipo = TipoReporte.builder()
                .nombreTipoReporte(dto.getNombreTipoReporte())
                .build();

        tipoReporteRepository.save(tipo);

        return mapToResponse(tipo);
    }

    @Override
    public List<TipoReporteResponseDTO> listar() {

        return tipoReporteRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TipoReporteResponseDTO obtenerPorId(Long id) {

        TipoReporte tipo = tipoReporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tipo de reporte no encontrado"));

        return mapToResponse(tipo);
    }

    @Override
    public void eliminar(Long id) {
        tipoReporteRepository.deleteById(id);
    }

    private TipoReporteResponseDTO mapToResponse(TipoReporte t) {

        return TipoReporteResponseDTO.builder()
                .id(t.getId())
                .nombreTipoReporte(t.getNombreTipoReporte())
                .build();
    }

}