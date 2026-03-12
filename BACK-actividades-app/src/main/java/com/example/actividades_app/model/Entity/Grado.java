package com.example.actividades_app.model.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "grado")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gradoID")
    private Long id;

    @Column(name = "numero_grado", nullable = false)
    private String numGrado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nivelID", nullable = false)
    private Nivel nivel;

    @OneToMany(mappedBy = "grado", fetch = FetchType.LAZY)
    private List<Seccion> secciones;
}