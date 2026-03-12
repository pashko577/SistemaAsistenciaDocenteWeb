package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "especialidad_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadDocente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "especialidadDocenteID")
    private Long id;

    @Column(name = "nombreEspecialidad", nullable = false, length = 100)
    private String nombreEspecialidad;

    // Relación con docentes
    @OneToMany(mappedBy = "especialidadDocente")
    private List<Docente> docentes;
}