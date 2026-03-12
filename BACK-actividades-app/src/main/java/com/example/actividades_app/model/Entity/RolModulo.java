package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rol_modulo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolModulo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rolModuloID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rolID", nullable = false)
    private Rol rol;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moduloID", nullable = false)
    private Modulo modulo;
}