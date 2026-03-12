package com.example.actividades_app.model.Entity;

import com.example.actividades_app.enums.Estado;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Docente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "docenteID")
    private Long id;

    @Column(name = "observaciones", length = 255)
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "especialidadDocenteID", nullable = false)
    private EspecialidadDocente especialidadDocente;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private Estado estado;

    @OneToOne
    @JoinColumn(name = "usuarioID", nullable = false, unique = true)
    private Usuario usuario;

    @OneToMany(mappedBy = "docente", fetch = FetchType.LAZY)
    private List<AsignacionDocente> asignaciones;

    @OneToMany(mappedBy = "docenteTitular", fetch = FetchType.LAZY)
    private List<ReemplazoDocente> reemplazosTitular;

    @OneToMany(mappedBy = "docenteReemplazo", fetch = FetchType.LAZY)
    private List<ReemplazoDocente> reemplazosRealizados;
}