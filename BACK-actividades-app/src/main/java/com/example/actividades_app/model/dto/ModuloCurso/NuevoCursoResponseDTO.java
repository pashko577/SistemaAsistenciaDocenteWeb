package com.example.actividades_app.model.dto.ModuloCurso;

import lombok.Data;

@Data
public class NuevoCursoResponseDTO {

    private Long id;

    private Long cursoID;          
    private Long nivelID;
    private Long gradoID; 
    private Long seccionID;
    private String aula;
    private Long docenteID;

}
