package com.example.actividades_app.model.dto.ModuloUsuario;

import java.util.List;

import com.example.actividades_app.model.dto.Permisos.ModuloRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private String token;
    private String dni;
    private List<String> roles;
    private String message;
    private List<ModuloRequestDTO> rutas_permitidas; 
}