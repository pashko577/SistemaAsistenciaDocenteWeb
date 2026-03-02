package com.example.actividades_app.model.dto.Reporte;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CronogramaDiarioRequestDTO {

    @NotNull(message = "La fecha del reporte es obligatoria")
    private String fechaReporte;
    @NotBlank(message = "El título del reporte es obligatorio")
    private String tituloReporte;
    @NotBlank(message = "La frase del día es obligatoria")
    private String fraseDelDia;
    @NotBlank(message = "Las instrucciones son obligatorias")
    private String instrucciones;

    @NotBlank(message = "La hora de inicio es obligatoria")
    private String horaInicioClase;
    @NotBlank(message = "La hora de fin es obligatoria")
    private String horaFinClase;
    @NotNull(message = "La clase es obligatoria")
    private Long claseID;
}
