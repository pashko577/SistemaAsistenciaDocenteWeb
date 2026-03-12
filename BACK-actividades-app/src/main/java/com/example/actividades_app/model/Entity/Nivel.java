package com.example.actividades_app.model.Entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "nivel")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Nivel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nivelID")
    private Long id;

    @Column(name = "nombre_nivel", nullable = false, length = 50, unique = true)
    private String nomNivel;

    @OneToMany(mappedBy = "nivel", fetch = FetchType.LAZY)
    private List<Grado> grados;
}