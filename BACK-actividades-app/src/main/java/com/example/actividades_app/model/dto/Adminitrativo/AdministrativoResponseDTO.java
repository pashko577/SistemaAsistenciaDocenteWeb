package com.example.actividades_app.model.dto.Adminitrativo;

import com.example.actividades_app.enums.Estado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativoResponseDTO {

    private Long id;

    private Long usuarioId;
    private String nombreUsuario; // opcional si quieres mostrarlo

    private Long cargoAdministrativoId;
    private String nombreCargo;

    private Estado estado;
}
