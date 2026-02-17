package com.example.actividades_app.model;

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
    private Long claseID;

    @Column(name = "tiempo_clase", nullable = false)
    private Integer tiempoClase;
    // Ejemplo: 1, 2, 3 (hora académica)

    @Column(name = "tema", length = 255)
    private String tema;

    @Column(name = "hora_efectiva")
    private Integer horaEfectiva;
    // Ejemplo: cantidad real de horas dictadas

    @Column(name = "aula", length = 50)
    private String aula;

    // RELACIONES

    // Muchos Clase pertenecen a un Docente
    @ManyToOne
    @JoinColumn(name = "docenteID", nullable = false)
    private Docente docente;

    // Muchos Clase pertenecen a un Curso
    @ManyToOne
    @JoinColumn(name = "cursoID", nullable = false)
    private Curso curso;

    // Muchos Clase pertenecen a un Grado
    @ManyToOne
    @JoinColumn(name = "gradoID", nullable = false)
    private Grado grado;

    // Muchos Clase pertenecen a una Seccion
    @ManyToOne
    @JoinColumn(name = "seccionID", nullable = false)
    private Seccion seccion;

    // Muchos Clase pertenecen a un Nivel
    @ManyToOne
    @JoinColumn(name = "nivelID", nullable = false)
    private Nivel nivel;
}
