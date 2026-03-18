package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoResponseDTO;
import com.example.actividades_app.repository.TipoDocumentoRepository;
import com.example.actividades_app.service.TipoDocumentoService;

import lombok.RequiredArgsConstructor;
@Service
@Transactional
@RequiredArgsConstructor
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

    private final TipoDocumentoRepository tipoDocumentoRepository;

    @Override
    public TipoDocumentoResponseDTO crearTipoDocumento(TipoDocumentoRequestDTO dto) {

        if (tipoDocumentoRepository.existsByNombreTD(dto.getNombreTD())) {
            throw new RuntimeException("El Tipo Documento ya existe");
        }

        TipoDocumento tipoDocumento = TipoDocumento.builder()
                .nombreTD(dto.getNombreTD())
                .build();

        TipoDocumento guardado = tipoDocumentoRepository.save(tipoDocumento);

        return mapToResponse(guardado);
    }

    @Override
    public TipoDocumentoResponseDTO obtenerPorId(Long tipoDocumentoId) {

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo Documento no encontrado"));

        return mapToResponse(tipoDocumento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TipoDocumentoResponseDTO> listarTodas() {

        return tipoDocumentoRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void eliminarTipoDocumento(Long tipoDocumentoId) {

        TipoDocumento tipoDocumento = tipoDocumentoRepository.findById(tipoDocumentoId)
                .orElseThrow(() -> new RuntimeException("Tipo Documento no encontrado"));

        try {
            tipoDocumentoRepository.delete(tipoDocumento);
        } catch (Exception e) {
            throw new RuntimeException("No se puede eliminar el TipoDocumento porque tiene registros asociados");
        }
    }

    private TipoDocumentoResponseDTO mapToResponse(TipoDocumento tipoDocumento) {
        return TipoDocumentoResponseDTO.builder()
                .id(tipoDocumento.getId())
                .nombreTD(tipoDocumento.getNombreTD())
                .build();
    }
}