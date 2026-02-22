package com.example.actividades_app.model.dto.Reporte;

import java.util.List;

import lombok.Data;

@Data
public class ResumenGeneralResponseDTO {
    private String codigo;
    private String dni;
    private String sede;
    private String cargo;
    private String tipoPago;

    private Double pagoBase; 
    private Double netoAPagar;
    private Double totalBonificaciones;
    private Double totalDescuentos;

    private Double tarifaxHora;

    private /* me quede aqui bbs */
    private List<DetalleBonificacionResponseDTO> bonificaciones;
    private List<DetalleDescuentoResponseDTO> descuentos;
}
