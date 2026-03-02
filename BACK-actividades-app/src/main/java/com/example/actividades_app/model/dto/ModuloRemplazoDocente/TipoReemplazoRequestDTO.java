package com.example.actividades_app.model.dto.ModuloRemplazoDocente;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TipoReemplazoRequestDTO {
    @NotBlank(message = "El nombre del tipo de reemplazo es obligatorio")
    private String nombreTipoReemplazo;
}
