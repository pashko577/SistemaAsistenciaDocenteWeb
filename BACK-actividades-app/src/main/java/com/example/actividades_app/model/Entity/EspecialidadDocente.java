package com.example.actividades_app.model.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "especialidad_docente")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EspecialidadDocente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "especialidad_DocenteID")
    private Long especialidadDocenteID;

    @Column(name = "nombre_especialidad", nullable = false, length = 100)
    private String nombreEspecialidad;
}
