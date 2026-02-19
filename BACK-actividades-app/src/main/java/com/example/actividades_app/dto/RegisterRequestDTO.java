package com.example.actividades_app.dto;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDTO {

    @NotBlank
    private String dni;

    @NotBlank
    private String password;

    @NotBlank
    private Long sedeId;
    private Set<String> roles;
}
