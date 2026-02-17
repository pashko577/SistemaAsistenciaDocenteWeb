package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;


import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarAsistenciaResponseDTO {

    private Long id;

    private Long docenteId;
    private String docenteNombre;

    private LocalDate fecha;
    private LocalTime horaIngreso;
    private LocalTime horaSalida;

    private int horasTrabajadas;

    private String observaciones;
    private String materialClase;
    private boolean usoTerno;

}
