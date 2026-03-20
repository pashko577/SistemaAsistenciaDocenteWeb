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
    private Integer diasLaborablesMes;
    private Long usuarioId;
    
    // --- Campos Nuevos para el Frontend ---
    private String usuarioNombre;     // Ejemplo: "Juan Pérez"
    private String usuarioDni;        // Ejemplo: "74859612"
    private String nombreActividad;   // Ejemplo: "Docente Matemática"
    private String tipoPlanilla;      // Ejemplo: "DOCENTE" o "ADMINISTRATIVO"
    // --------------------------------------

    private Long tipoActividadId;
    private Estado estado;
}