package com.example.actividades_app.service.Impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.actividades_app.model.Entity.TipoActividad;
import com.example.actividades_app.model.dto.Contrato.TipoActividadRequestDTO;
import com.example.actividades_app.model.dto.Contrato.TipoActividadResponseDTO;
import com.example.actividades_app.repository.TipoActividadRepository;
import com.example.actividades_app.service.TipoActividadService;

@Service
@RequiredArgsConstructor
public class TipoActividadServiceImpl implements TipoActividadService {

    private final TipoActividadRepository tipoActividadRepository;

    @Override
    public TipoActividadResponseDTO crear(TipoActividadRequestDTO dto) {

        TipoActividad tipo = TipoActividad.builder()
                .nombre(dto.getNombre())
                .tipoPlanilla(dto.getTipoPlanilla())
                .build();

        tipoActividadRepository.save(tipo);

        return TipoActividadResponseDTO.builder()
                .id(tipo.getId())
                .nombre(tipo.getNombre())
                .tipoPlanilla(tipo.getTipoPlanilla())
                .build();
    }

    @Override
    public List<TipoActividadResponseDTO> listar() {

        return tipoActividadRepository.findAll()
                .stream()
                .map(tipo -> TipoActividadResponseDTO.builder()
                        .id(tipo.getId())
                        .nombre(tipo.getNombre())
                        .tipoPlanilla(tipo.getTipoPlanilla())
                        .build())
                .collect(Collectors.toList());
    }
}