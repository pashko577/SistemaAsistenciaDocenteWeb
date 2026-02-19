package com.example.actividades_app.model.Entity;

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
}
