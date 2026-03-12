package com.example.actividades_app.model.Entity;


import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "periodo_academico")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodoAcademico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "periodoAcademicoID")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "fechaInicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fechaFin", nullable = false)
    private LocalDate fechaFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoPeriodo estado;

    public enum EstadoPeriodo {
        ACTIVO,
        FINALIZADO,
        INACTIVO
    }
}