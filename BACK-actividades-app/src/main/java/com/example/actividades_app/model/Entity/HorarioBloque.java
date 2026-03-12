package com.example.actividades_app.model.Entity;

import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalTime;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "horario_bloque")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioBloque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "horarioBloqueID")
    private Long id;

    @Column(name = "horaInicio", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime horaInicio;

    @Column(name = "horaFin", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime horaFin;

    @Column(name = "ordenBloque", nullable = false)
    private Integer ordenBloque;

    @OneToMany(mappedBy = "horarioBloque")
    private List<CronogramaDocente> cronogramas;
}