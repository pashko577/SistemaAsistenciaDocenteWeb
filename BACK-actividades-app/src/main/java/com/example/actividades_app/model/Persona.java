package com.example.actividades_app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "persona")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "personaID")
    private Long id;

    @NotBlank(message = "Los nombres son obligatorios")
    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 15)
    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;

    @NotBlank(message = "El celular es obligatorio")
    @Column(name = "celular", nullable = false, length = 15)
    private String celular;

    @NotBlank(message = "La dirección es obligatoria")
    @Column(name = "direccion", nullable = false, length = 200)
    private String direccion;

    // FK -> TipoDocumento
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_DocumentoID", nullable = false)
    private TipoDocumento tipoDocumento;

    @Email(message = "Email no válido")
    @NotBlank(message = "El email es obligatorio")
    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;
}
