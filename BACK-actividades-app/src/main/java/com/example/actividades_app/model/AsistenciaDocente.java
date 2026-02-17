package com.example.actividades_app.model;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;

@Entity
@Table(name = "asistencia_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asistencia_DocenteID")
    private Long id;

    @Column(name = "horaEntradaDoc")
    private LocalTime horaEntradaDoc;

    @Column(name = "horaSalidaDoc")
    private LocalTime horaSalidaDoc;

    @Column(name = "observacion", length = 250)
    private String observacion;

    @Column(name = "materialClase")
    private Boolean materialClase;

    @Column(name = "usoTerno")
    private Boolean usoTerno;

    // FK -> Docente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteID", nullable = false)
    private Docente docente;

    // FK -> CronogramaDiario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronograma_DiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

}
