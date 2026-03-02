package com.example.actividades_app.model.dto.Contrato;
import java.math.BigDecimal;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Contrato.TipoPago;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ContratoResponseDTO {

    private Long id;

    private TipoPago tipoPago;

    private BigDecimal montoBase;

    private Long usuarioId;

    private Estado estado;
}
