package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClaseResponseDTO {

    private Long id;

    private Integer tiempoClase;




    private String aula;

    private Long cursoId;
    private String cursoNombre;

    private Long seccionId;
    private String seccionNombre;

    private Long periodoAcademicoId;
    private String periodoNombre;

}
