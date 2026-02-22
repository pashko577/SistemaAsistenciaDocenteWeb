package com.example.actividades_app.model.dto.Adminitrativo;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class AdministrativoRegistroAsistenciaResponseDTO {
    private LocalDate fecha;
    private LocalTime horaIngreso;
    private LocalTime salidaAlmuerzo;
    private LocalTime retornoAlmuerzo;
    private LocalTime horaSalida;
    private boolean usoTerno;
    private String observaciones;
    private Integer tardanzaMinutos;
    private String estado;
}
