package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "reporte_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reporte_DocenteID")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "observaciones", length = 300)
    private String observaciones;

    @Column(name = "tardanza")
    private Integer tardanza; // minutos

    @Column(name = "publiWSS")
    private Boolean publiWSS;

    @Column(name = "publiWFS")
    private Boolean publiWFS;

    @Column(name = "agendaE")
    private Boolean agendaE;

    @Column(name = "tema", length = 200)
    private String tema;

    // FK -> Docente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteID", nullable = false)
    private Docente docente;

    // FK -> TipoAsistencia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_AsistenciaID", nullable = false)
    private TipoAsistencia tipoAsistencia;

    // FK -> CronogramaDiario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronograma_DiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

}
