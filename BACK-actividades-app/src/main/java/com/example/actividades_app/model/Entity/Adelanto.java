package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

import com.example.actividades_app.enums.EstadoAdelanto;
@Entity
@Table(name = "adelanto")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Adelanto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adelantoID")
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre; // Ej: "Adelanto Quincena Marzo"

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING) // Importante para guardar el texto (PENDIENTE/APLICADO)
    @Column(name = "estado", nullable = false)
    private EstadoAdelanto estado;

    // RELACIÓN CON USUARIO (La que unificamos)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    // RELACIÓN CON PAGO (Ahora es opcional: nullable = true)
    // Solo se llena cuando el estado pasa a APLICADO
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagoID", nullable = true)
    private Pago pago;
}