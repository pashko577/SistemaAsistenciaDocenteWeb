package com.example.actividades_app.model.Entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.actividades_app.enums.EstadoReemplazo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reemplazo_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReemplazoDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reemplazoDocenteID")
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cronogramaDiarioID", nullable = false)
    private CronogramaDiario cronogramaDiario;

    @Column(name = "motivo", length = 250)
    private String motivo;

    // Tipo reemplazo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipoReemplazoID", nullable = false)
    private TipoReemplazo tipoReemplazo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private EstadoReemplazo estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuarioRegistroID", nullable = false)
    private Usuario usuarioRegistro;

    @Column(name = "fechaRegistro", nullable = false)
    private LocalDate fechaRegistro;


}

