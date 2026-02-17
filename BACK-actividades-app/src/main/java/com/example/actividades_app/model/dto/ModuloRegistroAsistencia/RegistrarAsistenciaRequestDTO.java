package com.example.actividades_app.model.dto.ModuloRegistroAsistencia;



import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarAsistenciaRequestDTO {

    private Long docenteId;
    private LocalDate fecha;
    private LocalTime horaIngreso;
    private LocalTime horaSalida;

    private String observaciones;
    private String materialClase;
    private boolean usoTerno;

}
