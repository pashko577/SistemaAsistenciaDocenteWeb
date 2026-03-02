package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import lombok.Data;

@Data
public class PermisoResponseDTO {
    private Long permisoId;
    private Long docenteId;
    private String docenteNombre;
    private String fecha;
    private String motivo;
    private String tipo;
    private String estado; // PENDIENTE, APROBADO, RECHAZADO
}
