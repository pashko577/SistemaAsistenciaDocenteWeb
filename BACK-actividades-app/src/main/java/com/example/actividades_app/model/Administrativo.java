package com.example.actividades_app.model;

import com.example.actividades_app.enums.Estado;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "administrativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Administrativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "administrativoID")
    private Long administrativoID;

    @OneToOne
    @JoinColumn(name = "usuarioID", nullable = false, unique = true)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "cargo_AdministrativoID", nullable = false)
    private CargoAdministrativo cargoAdministrativo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;
}
