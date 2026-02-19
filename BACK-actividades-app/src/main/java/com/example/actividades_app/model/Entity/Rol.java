package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="rolID", nullable=false )
    private Long id;

    @Column(name = "nombreRol", nullable = false, unique = true)
    private String nombreRol;
}

