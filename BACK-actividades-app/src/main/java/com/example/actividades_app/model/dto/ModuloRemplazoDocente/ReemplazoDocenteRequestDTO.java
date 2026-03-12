package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import jakarta.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ReemplazoDocenteRequestDTO {
    @NotNull(message = "El docente titular es obligatorio")
    private Long docenteTitularId;

    @NotNull(message = "El docente reemplazo es obligatorio")
    private Long docenteReemplazoId;

    @NotNull(message = "El cronograma es obligatorio")
    private Long cronogramaDiarioId;


    private String motivo;

    @NotNull(message = "El tipo de reemplazo es obligatorio")
    private Long tipoReemplazoId;
}

