package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleReporteDocenteResponseDTO {

    private Long id;

    private Long reporteDocenteId;

    private Boolean publiWSS;

    private Boolean publiWFS;

    private Boolean agendaE;

}