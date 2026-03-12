package com.example.actividades_app.model.dto.ModuloDocente;


import com.example.actividades_app.enums.Estado;
import lombok.Data;

@Data
public class AsignacionDocenteRequestDTO {

    private Estado estado;
    private String observaciones;

    private Long docenteId;
    private Long claseId;
    private Long tipoActividadId;

}