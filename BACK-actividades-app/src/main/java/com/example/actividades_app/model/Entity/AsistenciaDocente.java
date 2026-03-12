package com.example.actividades_app.model.Entity;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalTime;
@Entity
@Table(
    name = "asistencia_docente",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "cronogramaDiarioID")
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asistenciaDocenteID")
    private Long id;

    @Column(name = "horaEntradaDoc", columnDefinition = "TIME(0)")
    private LocalTime horaEntradaDoc;

    @Column(name = "horaSalidaDoc", columnDefinition = "TIME(0)")
    private LocalTime horaSalidaDoc;

    @Column(name = "observacion", length = 250)
    private String observacion;

    @Column(name = "materialClase")
    private Boolean materialClase;

    @Column(name = "usoTerno")
    private Boolean usoTerno;

    @Enumerated(EnumType.STRING)
    @Column(name = "estadoAsistencia", nullable = false)
    private EstadoAsistenciaDocente estadoAsistencia;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronogramaDiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

}