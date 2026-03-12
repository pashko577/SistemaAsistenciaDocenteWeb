package com.example.actividades_app.model.dto.ModuloCurso;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GradoResponseDTO {

    private Long id;
    private String numGrado;

    private Long nivelId;

    private String nivelNombre;

}
