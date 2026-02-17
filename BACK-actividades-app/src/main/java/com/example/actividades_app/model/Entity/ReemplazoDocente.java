package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;
@Entity
@Table(name = "reemplazo_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReemplazoDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reemplazo_DocenteID")
    private Long id;

    // Docente titular
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteTitularID", nullable = false)
    private Docente docenteTitular;

    // Docente reemplazo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docenteReemplazoID", nullable = false)
    private Docente docenteReemplazo;

    // Cronograma diario
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronograma_DiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "motivo", length = 250)
    private String motivo;

    // Tipo reemplazo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_ReemplazoID", nullable = false)
    private TipoReemplazo tipoReemplazo;

    // Estado ENUM
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReemplazo estado;

    // Usuario que registra
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioRegistroID", nullable = false)
    private Usuario usuarioRegistro;

    @Column(name = "fechaRegistro", nullable = false)
    private LocalDateTime fechaRegistro;


    public enum EstadoReemplazo {

        PENDIENTE,
        APROBADO,
        RECHAZADO,
        FINALIZADO

    }

}
