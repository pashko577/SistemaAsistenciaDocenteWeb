package com.example.actividades_app.model.dto.Reporte;

import lombok.Data;

@Data
public class CronogramaDiarioResponseDTO {

    private Long cronogramaId;
    private String fechaReporte;
    private String tituloReporte;
    private String fraseDelDia;
    private String instrucciones;
    private String horaInicioClase;
    private String horaFinClase;
    // Datos de la clase
    private Long claseId;
    private String tema;
    private String tiempoClase;
    private String horaEfectiva;
    // Datos del curso, grado, sección y nivel
    private Long cursoId;
    private String cursoNombre;
    private Long gradoId;
    private String gradoNombre;
    private Long seccionId;
    private String seccionNombre;
    private Long nivelId;
    private String nivelNombre;
    //docente
    private Long docenteId;
    private String docenteNombre;
}
