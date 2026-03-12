package com.example.actividades_app.model.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "curso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cursoID")
    private Long id;

    @Column(name = "nombre_curso", nullable = false, length = 100, unique = true)
    private String nombreCurso;

    @OneToMany(mappedBy = "curso", fetch = FetchType.LAZY)
    private List<Clase> clases;
}