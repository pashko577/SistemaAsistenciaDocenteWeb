/* package com.example.actividades_app.model.relation;

import java.time.LocalDate;

import com.example.actividades_app.model.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "proyecto_usuario")
public class ProyectoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "proyecto_usuario_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "proyecto_id", 
        nullable = false
    )
    private ProyectoUsuario proyecto;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "usuario_id",
        nullable = false
    )
    private Usuario usuario;

    @Column(name = "fecha_asignacion", nullable = false)
    private LocalDate fechaAsignacion; 
}
 */