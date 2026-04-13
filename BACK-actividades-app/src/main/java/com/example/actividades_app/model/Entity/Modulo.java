package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Modulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "moduloID")
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(length = 100)
    private String ruta;

    @Column(length = 50)
private String icono; 
}