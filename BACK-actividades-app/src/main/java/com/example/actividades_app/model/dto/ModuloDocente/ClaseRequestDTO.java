package com.example.actividades_app.model.dto.ModuloDocente;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaseRequestDTO {

    private Long cursoId;
    private Long gradoId;
    private Long seccionId;
    private Long nivelId;
    private Long aulaId;

    private String tema;
    private String horaEfectiva;
    private int tiempoClase;
    private String aula;

}
