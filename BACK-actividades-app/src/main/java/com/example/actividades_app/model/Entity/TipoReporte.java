package com.example.actividades_app.model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tipo_reporte")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoReporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoReporteID")
    private Long id;

    @Column(name = "nombreTipoReporte", nullable = false, length = 50)
    private String nombreTipoReporte; 
    // ESCOLAR, ACADEMIA, TUTORIA
}

