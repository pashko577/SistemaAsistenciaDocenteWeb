package com.example.actividades_app.service;

import java.util.List;


import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoResponseDTO;

public interface TipoDocumentoService {

    TipoDocumentoResponseDTO crearTipoDocumento(TipoDocumentoRequestDTO dto);

    TipoDocumentoResponseDTO obtenerPorId(Long tipoDocumentoId);

    List<TipoDocumentoResponseDTO> listarTodas();

    void eliminarTipoDocumento(Long tipoDocumentoId);
}
