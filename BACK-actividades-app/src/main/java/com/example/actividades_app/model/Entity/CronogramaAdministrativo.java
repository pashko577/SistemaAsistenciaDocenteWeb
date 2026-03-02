package com.example.actividades_app.model.Entity;

import java.time.LocalDate;
import java.time.LocalTime;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cronograma_administrativo")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CronogramaAdministrativo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cronograma_AdministrativoID")
    private Long id;

    @Column(name = "hora_entrada", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime horaEntrada;

   @Column(name = "hora_salida", nullable = false, columnDefinition = "TIME(0)")
    private LocalTime horaSalida;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "administrativoID", nullable = false)
    private Administrativo administrativo;
}
