package com.example.actividades_app.model.dto.ModuloUsuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoDocumentoRequestDTO {

    @NotBlank(message = "El nombre del TipoDocumento es obligatorio")
    private String nombreTD;
}
