package com.example.actividades_app.model.dto.Contrato;
import java.math.BigDecimal;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Contrato.TipoPago;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContratoResponseDTO {

    private Long id;

    private TipoPago tipoPago;

    private BigDecimal montoBase;

    private Integer horasJornada;

    private Integer diasLaboralesMes;

    private Long usuarioId;

    private Long tipoActividadId;

    private Estado estado;
}