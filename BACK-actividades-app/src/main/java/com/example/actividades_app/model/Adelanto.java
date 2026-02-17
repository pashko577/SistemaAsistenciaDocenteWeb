package com.example.actividades_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

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
    private String nombre;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // FK -> Pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagoID", nullable = false)
    private Pago pago;

}
