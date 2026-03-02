package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import lombok.Data;

@Data
public class ReemplazoDocenteResponseDTO {
    private Long reemplazoId;
    private Long docenteTitularId;
    private String docenteTitularNombre;
    private Long docenteReemplazoId;
    private String docenteReemplazoNombre;
    private Long cronogramaDiarioId;
    private String fecha;
    private String motivo;
    private Long tipoReemplazoId;
    private String tipoReemplazoNombre;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO, FINALIZADO
    private Long usuarioRegistroId;
    private String usuarioRegistroNombre;
    private String fechaRegistro;
}
