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
public class AdministrativoRequestDTO {

    private Long usuarioId;
    private Long cargoAdministrativo;
    private Estado estado;
}
