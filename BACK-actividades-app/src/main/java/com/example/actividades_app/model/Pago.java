package com.example.actividades_app.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "pago")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pagoID")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipoPago", nullable = false)
    private TipoPago tipoPago;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "montoActividad", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoActividad;

    @Column(name = "netoPagar", nullable = false, precision = 10, scale = 2)
    private BigDecimal netoPagar;

    // FK -> Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioID", nullable = false)
    private Usuario usuario;


    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Adelanto> adelantos;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deduccion> deducciones;

    @OneToMany(mappedBy = "pago", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Bonificacion> bonificaciones;

    public enum TipoPago {
        POR_HORA,
        MENSUAL
    }
}
