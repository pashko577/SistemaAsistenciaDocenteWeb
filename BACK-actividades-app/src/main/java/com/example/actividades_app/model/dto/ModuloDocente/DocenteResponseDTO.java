package com.example.actividades_app.model.dto.ModuloDocente;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocenteResponseDTO {

    private Long id;

    private String dni;
    private String nombres;
    private String apellidos;

    private String email;
    private String celular;

    private int horasContratadas;
    private double pagoPorHora;

    private String especialidadNombre;

    private String estado;
    private String observaciones;
}
