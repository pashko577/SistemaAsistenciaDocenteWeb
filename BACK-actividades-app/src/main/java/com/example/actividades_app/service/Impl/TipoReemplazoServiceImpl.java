package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.TipoReemplazo;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoRequestDTO;
import com.example.actividades_app.model.dto.ModuloRemplazoDocente.TipoReemplazoResponseDTO;
import com.example.actividades_app.repository.TipoReemplazoRepository;
import com.example.actividades_app.service.TipoReemplazoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TipoReemplazoServiceImpl implements TipoReemplazoService {

    private final TipoReemplazoRepository tipoReemplazoRepository;

    @Override
    public TipoReemplazoResponseDTO crear(TipoReemplazoRequestDTO dto) {

        if (tipoReemplazoRepository.existsByNombreTipoReemplazo(dto.getNombreTipoReemplazo())) {
            throw new RuntimeException("El tipo de reemplazo ya existe");
        }

        TipoReemplazo tipo = TipoReemplazo.builder()
                .nombreTipoReemplazo(dto.getNombreTipoReemplazo())
                .build();

        tipoReemplazoRepository.save(tipo);

        return mapToResponse(tipo);
    }

    @Override
    public List<TipoReemplazoResponseDTO> listar() {

        return tipoReemplazoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TipoReemplazoResponseDTO mapToResponse(TipoReemplazo t) {

        return TipoReemplazoResponseDTO.builder()
                .id(t.getId())
                .nombreTipoReemplazo(t.getNombreTipoReemplazo())
                .build();
    }

}