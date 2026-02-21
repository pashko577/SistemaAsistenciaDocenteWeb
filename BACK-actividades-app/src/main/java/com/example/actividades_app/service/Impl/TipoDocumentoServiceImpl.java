package com.example.actividades_app.service.Impl;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;
import com.example.actividades_app.repository.TipoDocumentoRepository;
import com.example.actividades_app.service.TipoDocumentoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TipoDocumentoServiceImpl implements TipoDocumentoService{

    private final TipoDocumentoRepository tipoDocumentoRepository;

     @Override
     public TipoDocumento crearTipoDocumento(TipoDocumentoRequestDTO dto){
        if (tipoDocumentoRepository.existsByNombreTD(dto.getNombreTD())) {
            throw new RuntimeException("El Tipo Documento ya existe");            
        }

        TipoDocumento tipoDocumento = TipoDocumento.builder()
            .nombreTD(dto.getNombreTD())
            .build();

        return tipoDocumentoRepository.save(tipoDocumento);
    }


}
