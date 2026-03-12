package com.example.actividades_app.model.Entity;



import com.example.actividades_app.enums.TipoPlanilla;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_actividad")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoActividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipoActividadID")
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoPlanilla tipoPlanilla;
}