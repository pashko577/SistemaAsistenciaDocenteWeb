package com.example.actividades_app.model.dto.ModuloDocente;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DocenteResponseDTO {
    private Long id;
    private String dni;
    private Long usuarioId; // <--- Agrega este campo
    private Long sedeId; // <--- AGREGAR ESTO
    private String nombreSede;
    private Long especialidadId; // <--- AGREGAR ESTO
    private String nombres;
    private String apellidos;
    private String email;
    private String celular;
    private String direccion;
    private String nombreEspecialidad; // Para mostrar el nombre, no solo el ID
    private String tipoDocumentoNombre;
    private String estado;
    private String observaciones;
}