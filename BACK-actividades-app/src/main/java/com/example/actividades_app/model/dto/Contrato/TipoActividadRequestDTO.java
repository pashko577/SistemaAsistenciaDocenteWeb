package com.example.actividades_app.model.dto.Contrato;

import com.example.actividades_app.enums.TipoPlanilla;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoActividadRequestDTO {

    private String nombre;
    private TipoPlanilla tipoPlanilla;
}