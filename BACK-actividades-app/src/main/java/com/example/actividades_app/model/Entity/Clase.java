package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "clase")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Clase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "claseID")
    private Long id;

    @Column(name = "tiempoClase", nullable = false)
    private Integer tiempoClase;

    @Column(name = "aula", length = 50)
    private String aula;

    // RELACIONES

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cursoID", nullable = false)
    private Curso curso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seccionID", nullable = false)
    private Seccion seccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodoAcademicoID", nullable = false)
    private PeriodoAcademico periodoAcademico;
}