package com.example.actividades_app.model.dto.ModuloDocente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EspecialidadDocenteResponseDTO {
    private Long especialidadDocenteId; // Coincide con tu interfaz de Angular
    private String nombreEspecialidad;
}