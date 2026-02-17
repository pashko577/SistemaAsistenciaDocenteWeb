package com.example.actividades_app.model;

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
    private Long seccionID;

    @Column(name = "nombre_seccion", nullable = false, length = 10)
    private String nomSeccion;
}
