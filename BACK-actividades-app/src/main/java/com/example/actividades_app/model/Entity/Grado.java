package com.example.actividades_app.model.Entity;

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
    private Long gradoID;

    @Column(name = "numero_grado", nullable = false)
    private Integer numGrado;
}
