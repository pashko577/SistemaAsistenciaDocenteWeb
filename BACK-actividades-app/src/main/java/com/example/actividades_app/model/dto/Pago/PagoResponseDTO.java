package com.example.actividades_app.model.dto.Pago;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.dto.Reporte.AdelantoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.BonificacionResponseDTO;
import com.example.actividades_app.model.dto.Reporte.DescuentoResponseDTO;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagoResponseDTO {

    private Long pagoId;

    private LocalDate fecha;

    private Long contratoId;

    private String nombreCompleto; 
    private String dni;
    private String cargo;
    private String sede;

    private String tipoPago; // PAGO_HORA | PAGO_MENSUAL

    private BigDecimal montoBase;       // del contrato
    private BigDecimal montoActividad;  // calculado
    private BigDecimal netoPagar;       // resultado final

    private BigDecimal totalBonificaciones;
    private BigDecimal totalDeducciones;
    private BigDecimal totalAdelantos;

    // Detalles
    private List<AdelantoResponseDTO> adelantos;
    private List<BonificacionResponseDTO> bonificaciones;
    private List<DescuentoResponseDTO> deducciones;
}