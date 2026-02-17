package com.example.actividades_app.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tipo_asistencia")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoAsistencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tipo_AsistenciaID")
    private Long id;

    @Column(name = "nomTipoAsis", nullable = false, length = 100)
    private String nomTipoAsis;

}
