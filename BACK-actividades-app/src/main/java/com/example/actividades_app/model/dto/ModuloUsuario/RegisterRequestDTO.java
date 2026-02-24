package com.example.actividades_app.model.dto.ModuloUsuario;

import java.util.Set;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    // CAMBIO: @NotNull para Long
    @NotNull(message = "La sede es obligatoria")
    private Long sedeId;
    
    private Set<String> roles;

    // Persona:
    @NotBlank(message = "Los nombres son obligatorios")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    private String apellidos;

    @NotNull(message = "El celular es obligatorio")
    private Number celular;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    private String email;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccion;

    // CAMBIO: @NotNull para Long
    @NotNull(message = "El tipo de documento es obligatorio")
    private Long tipoDocumentoId;
}