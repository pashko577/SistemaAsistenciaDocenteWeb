package com.example.actividades_app.model.Entity;

import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "asistencia_administrativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsistenciaAdministrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "asistenciaID")
    private Long asistenciaID;

    @Column(name = "hora_ingreso")
    private LocalTime horaIngreso;

    @Column(name = "hora_salida")
    private LocalTime horaSalida;

    @Column(name = "salida_almuerzo")
    private LocalTime salidaAlmuerzo;

    @Column(name = "retorno_almuerzo")
    private LocalTime retornoAlmuerzo;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @Column(name = "tardanza")
    private Integer tardanza; // minutos de retraso

    @Column(name = "uso_terno")
    private Boolean terno;

    @ManyToOne
    @JoinColumn(name = "administrativoID", nullable = false)
    private Administrativo administrativo;

    @ManyToOne
    @JoinColumn(name = "cronograma_AdministrativoID", nullable = false)
    private CronogramaAdministrativo cronogramaAdministrativo;
}
