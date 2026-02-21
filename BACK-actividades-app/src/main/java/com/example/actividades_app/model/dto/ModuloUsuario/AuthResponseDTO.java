package com.example.actividades_app.model.dto.ModuloUsuario;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {

    private String token;
    private String username;
    private List<String> roles;
}
