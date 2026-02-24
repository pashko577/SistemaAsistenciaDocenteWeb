package com.example.actividades_app.model.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "contrato")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Contrato {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contratoID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPago", nullable = false)
    private TipoPago tipoPago;

    @Column(name = "montoBase", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoBase;

    // =========================
    // FK -> Usuario
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    // =========================
    // Relación con Pago
    // =========================
    @OneToMany(mappedBy = "contrato",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    private List<Pago> pagos;

    public enum TipoPago {
        PAGO_HORA,
        PAGO_MENSUAL
    }
}