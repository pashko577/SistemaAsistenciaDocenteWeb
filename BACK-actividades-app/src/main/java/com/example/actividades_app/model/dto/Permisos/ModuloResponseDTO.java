package com.example.actividades_app.model.dto.Permisos;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ModuloResponseDTO {

    private Long id;
    private String nombre;
    private String descripcion;
    private String ruta;
    private String icono; // <-- Esencial para el Sidebar de Angular
}