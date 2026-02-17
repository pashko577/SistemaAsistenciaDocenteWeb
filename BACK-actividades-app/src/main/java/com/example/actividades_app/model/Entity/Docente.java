package com.example.actividades_app.model.Entity;

import com.example.actividades_app.enums.Estado;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docenteID")
    private Long docenteID;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "especialidad_DocenteID", nullable = false)
    private EspecialidadDocente especialidadDocente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;

    @OneToOne
    @JoinColumn(name = "usuarioID", nullable = false, unique = true)
    private Usuario usuario;
}
