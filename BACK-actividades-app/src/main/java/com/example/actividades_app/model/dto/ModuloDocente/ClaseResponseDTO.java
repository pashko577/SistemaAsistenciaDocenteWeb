package com.example.actividades_app.model.dto.ModuloDocente;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaseResponseDTO {

    private Long id;

    private String curso;
    private String grado;
    private String seccion;
    private String nivel;
    private String docente;
    private String aula;

    private String tema;
    private String horaEfectiva;
    private int tiempoClase;

}
