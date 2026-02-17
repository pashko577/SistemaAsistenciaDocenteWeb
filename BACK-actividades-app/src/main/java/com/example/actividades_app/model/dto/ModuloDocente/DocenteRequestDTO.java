package com.example.actividades_app.model.dto.ModuloDocente;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocenteRequestDTO {

    private String dni;
    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private String direccion;

    private int horasContratadas;
    private double pagoPorHora;

    private Long especialidadId;

    private String estado;
    private String observaciones;
}
