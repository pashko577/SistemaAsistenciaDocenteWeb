package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(
    name = "cronograma_diario",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"claseID", "fecha", "horaInicioClase"})
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaDiario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cronograma_DiarioID")
    private Long id;

    @Column(name = "horaInicioClase", nullable = false)
    private LocalTime horaInicioClase;

    @Column(name = "horaFinClase", nullable = false)
    private LocalTime horaFinClase;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // FK -> Clase
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "claseID", nullable = false)
    private Clase clase;

}
