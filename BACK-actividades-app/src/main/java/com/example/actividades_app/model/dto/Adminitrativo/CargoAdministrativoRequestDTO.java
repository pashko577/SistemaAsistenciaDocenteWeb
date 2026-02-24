package com.example.actividades_app.model.dto.Adminitrativo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoAdministrativoRequestDTO {
    @NotBlank(message = "El Nombre del Cargo es necesario")
    private String nombreCargo;
}
