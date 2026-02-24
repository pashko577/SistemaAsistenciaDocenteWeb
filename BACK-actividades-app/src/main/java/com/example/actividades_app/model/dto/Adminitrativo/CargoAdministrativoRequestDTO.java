package com.example.actividades_app.model.dto.Adminitrativo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoAdministrativoRequestDTO {
    private Long id;
    private String nombreCargo;
}
