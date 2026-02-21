package com.example.actividades_app.service;

import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.dto.ModuloUsuario.TipoDocumentoRequestDTO;

public interface TipoDocumentoService {
    TipoDocumento crearTipoDocumento(TipoDocumentoRequestDTO dto);
}
