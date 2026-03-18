package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "cronograma_diario", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "cronogramaDocenteID", "fecha" })
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cronogramaDiarioID")
    private Long id;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "estadoClase", nullable = false)
    private EstadoClase estadoClase = EstadoClase.PROGRAMADA;
    // FK -> CronogramaDocente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronogramaDocenteID", nullable = false)
    private CronogramaDocente cronogramaDocente;

    @Column(name = "tema", length = 255)
    private String tema;

    public enum EstadoClase {

        PROGRAMADA, // Clase planificada pero aún no dictada
        DICTADA, // Clase dictada normalmente
        CANCELADA, // Clase cancelada (feriado, suspensión, etc.)
        FALTO_DOCENTE,
        SUSPENDIDA,
        REPROGRAMADA,

    }
}
