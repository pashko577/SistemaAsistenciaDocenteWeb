package com.example.actividades_app.model.dto.ModuloCurso;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeccionResponseDTO {

    private Long id;
    private String nomSeccion;

    private Long gradoId;

    private String gradoNombre;

    private Long nivelId;
    private String nivelNombre;

}
