package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;

public interface TipoDocumentoService {
    TipoDocumento crearTipoDocumento(TipoDocumentoRequestDTO dto);
    TipoDocumento obtenerPorId(Long TipoDocumentoId);
    List<TipoDocumento> listarTodas();
    void eliminarTipoDocumento(Long TipoDocumentoId);
}
