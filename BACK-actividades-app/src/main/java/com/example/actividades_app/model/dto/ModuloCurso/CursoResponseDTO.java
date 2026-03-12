package com.example.actividades_app.model.dto.ModuloCurso;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CursoResponseDTO {

    private Long id;
    private String nombreCurso;

}
