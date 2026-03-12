package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TipoReemplazoResponseDTO {
    private Long id;
    private String nombreTipoReemplazo;
}
