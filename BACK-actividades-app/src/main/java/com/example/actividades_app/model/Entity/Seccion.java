package com.example.actividades_app.model.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "seccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seccionID")
    private Long id;

    @Column(name = "nombre_seccion", nullable = false, length = 10)
    private String nomSeccion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gradoID", nullable = false)
    private Grado grado;

    @OneToMany(mappedBy = "seccion", fetch = FetchType.LAZY)
    private List<Clase> clases;
}