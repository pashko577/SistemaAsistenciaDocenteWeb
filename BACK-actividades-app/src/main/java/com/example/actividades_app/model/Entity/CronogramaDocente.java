package com.example.actividades_app.model.Entity;

import java.util.List;

import com.example.actividades_app.enums.DiaSemana;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(
    name = "cronograma_docente",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"docenteID", "diaSemana", "horarioBloqueID"}
        )
    }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cronogramaDocenteID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "diaSemana", nullable = false)
    private DiaSemana diaSemana;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignacionDocenteID", nullable = false)
    private AsignacionDocente asignacionDocente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "horarioBloqueID", nullable = false)
    private HorarioBloque horarioBloque;

  @OneToMany(mappedBy = "cronogramaDocente", fetch = FetchType.LAZY)
private List<CronogramaDiario> cronogramasDiarios;
}