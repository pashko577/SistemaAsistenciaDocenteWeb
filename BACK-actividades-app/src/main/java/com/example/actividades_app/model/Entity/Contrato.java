package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

import com.example.actividades_app.enums.Estado;

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

    @Column(name = "horasJornada", nullable = false)
    private Integer horasJornada;

    @Column(name = "diasLaborablesMes", nullable = false)
    private Integer diasLaborablesMes;

    // =========================
    // FK -> Usuario
    // =========================
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "tipoActividadID", nullable = false)
    private TipoActividad tipoActividad;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;
    // =========================
    // Relación con Pago
    // =========================
    @OneToMany(mappedBy = "contrato", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos;

    public enum TipoPago {
        PAGO_HORA,
        PAGO_MENSUAL
    }
}