package com.example.actividades_app.model.dto.ModuloDocente;


import com.example.actividades_app.enums.Estado;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AsignacionDocenteResponseDTO {

    private Long id;

    private Estado estado;
    private String observaciones;

    private Long docenteId;
    private String docenteNombre;

    private Long claseId;

    private Long tipoActividadId;
    private String tipoActividadNombre;

}