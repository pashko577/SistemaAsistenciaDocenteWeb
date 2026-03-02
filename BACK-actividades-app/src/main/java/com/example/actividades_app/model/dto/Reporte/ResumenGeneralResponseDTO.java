package com.example.actividades_app.model.dto.Reporte;

import java.util.List;

import com.example.actividades_app.model.Entity.Contrato;

import lombok.Builder;
import lombok.Data;

@Data

public class ResumenGeneralResponseDTO {
    private String codigo;
    private String dni;
    private String sede;
    private String cargo;
    private Contrato.TipoPago tipoPago;

    private Double pagoBase; 
    private Double netoAPagar;
    private Double totalBonificaciones;
    private Double totalDescuentos;

    //MENSUAL O XHORA
    private Double monto;


    private List<BonificacionResponseDTO> bonificaciones;
    private List<DescuentoResponseDTO> descuentos;
    private List<AdelantoResponseDTO> adelantos;

}
