/* package com.example.actividades_app.model.relation;

import com.example.actividades_app.model.Actividad;
import com.example.actividades_app.model.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "usuario_actividad", 
    uniqueConstraints = {@UniqueConstraint(columnNames = {"usuario_id", "actividad_id"})}
)
public class UsuarioActividad {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuario_actividad_id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "usuario_id",
        nullable = false
    )
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(
        name = "actividad_id",
        nullable = false
    )
    private Actividad actividad;

}
 */