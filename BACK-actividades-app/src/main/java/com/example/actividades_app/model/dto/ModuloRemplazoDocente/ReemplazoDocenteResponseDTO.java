package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import java.time.LocalDate;

import com.example.actividades_app.enums.EstadoReemplazo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReemplazoDocenteResponseDTO {

    private Long id;

    private Long docenteTitularId;
    private String docenteTitularNombre;

    private Long docenteReemplazoId;
    private String docenteReemplazoNombre;

    private Long cronogramaDiarioId;

    private String motivo;

    private Long tipoReemplazoId;
    private String tipoReemplazoNombre;

    private EstadoReemplazo estadoReemplazo;

    private Long usuarioRegistroId;

    private LocalDate fechaRegistro;

}