package com.example.actividades_app.model.dto.Pago;



import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PagoCalculoResponseDTO {

    private BigDecimal montoBase;

    private BigDecimal descuentoTardanza;

    private BigDecimal descuentoFaltas;

    private BigDecimal cumplimientoDescuento;

    private BigDecimal bonificaciones;

    private BigDecimal netoPagar;

}
