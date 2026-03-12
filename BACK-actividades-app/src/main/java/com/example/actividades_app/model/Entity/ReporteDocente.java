package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reporte_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reporteDocenteID")
    private Long id;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    @Column(name = "tardanza")
    private Integer tardanza;

    // FK -> CronogramaDiario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronogramaDiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

    // 🔥 NUEVA FK -> TipoReporte
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipoReporteID", nullable = false)
    private TipoReporte tipoReporte;

    // Relación 1 a 1 con Detalle
    @OneToOne(mappedBy = "reporteDocente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private DetalleReporteDocente detalle;
}
