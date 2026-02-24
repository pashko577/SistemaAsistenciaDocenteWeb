package com.example.actividades_app.model.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_reporte_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleReporteDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detalleReporteDocenteID")
    private Long id;

    @Column(name = "publiWSS")
    private Boolean publiWSS;

    @Column(name = "publiWFS")
    private Boolean publiWFS;

    @Column(name = "agendaE")
    private Boolean agendaE;

    // FK -> ReporteDocente
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporteDocenteID", nullable = false)
    private ReporteDocente reporteDocente;
}