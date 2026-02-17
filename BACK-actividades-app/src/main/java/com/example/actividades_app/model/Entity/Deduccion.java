package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "deduccion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Deduccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "deduccionID")
    private Long id;

    @Column(name = "observaciones", length = 250)
    private String observaciones;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    // FK -> Pago
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pagoID", nullable = false)
    private Pago pago;

    // FK -> TipoDeduccion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_DeduccionID", nullable = false)
    private TipoDeduccion tipoDeduccion;

}
