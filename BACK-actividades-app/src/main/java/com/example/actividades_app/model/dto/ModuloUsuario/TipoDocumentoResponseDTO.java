package com.example.actividades_app.model.dto.ModuloUsuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDocumentoResponseDTO {

    private Long id;
    private String nombreTD;

}
