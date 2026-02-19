package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cargo_administrativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargoAdministrativo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cargo_AdministrativoID")
    private Long id;

    @Column(name = "nombreCargo", nullable = false, length = 100)
    private String nombreCargo;
}
