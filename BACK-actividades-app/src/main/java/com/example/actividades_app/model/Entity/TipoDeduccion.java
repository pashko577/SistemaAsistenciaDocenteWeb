package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_deduccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoDeduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_DeduccionID")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

}
