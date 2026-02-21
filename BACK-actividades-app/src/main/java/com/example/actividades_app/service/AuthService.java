package com.example.actividades_app.service;

import com.example.actividades_app.model.dto.ModuloUsuario.AuthResponseDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.LoginRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.RegisterRequestDTO;

public interface AuthService {

    void register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
    
}
