package com.example.actividades_app.model.dto.Adminitrativo;

import com.example.actividades_app.enums.Estado;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativoRequestDTO {
     // Usuario:
    @NotBlank(message = "El DNI es obligatorio")
    private String dni;

    @NotBlank(message = "La contraseña es obligatoria")
    private String password;

    @NotNull(message = "La sede es obligatoria")
    private Long sedeId;

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

    @NotNull(message = "El tipo de documento es obligatorio")
    private Long tipoDocumentoId;

    //Administrativod
    @NotNull(message = "El Cargo Administrativo es obligatorio")
    private Long cargoAdministrativoID;
    private Estado estado;
}
