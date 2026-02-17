package com.example.actividades_app.model;

import jakarta.persistence.*;
//import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

//import java.util.List;
import java.util.Set;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioID", nullable = false)
    private Long id;

    @NotBlank
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "personaID", nullable = false)
    private Persona persona;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sedeID", nullable = false)
    private Sede sede;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Rol.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "usuario_rol", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles;
}
/*
 * //Conexion ProyectoUsuario
 * 
 * @OneToMany(
 * mappedBy = "usuario",
 * cascade = CascadeType.ALL,
 * orphanRemoval = false
 * )
 * private List<ProyectoUsuario> proyectos;
 * 
 * //Conexion UsuarioActividad
 * 
 * @OneToMany(
 * mappedBy = "usuario",
 * cascade = CascadeType.ALL,
 * orphanRemoval = false
 * )
 * private List<UsuarioActividad> actividades;
 */