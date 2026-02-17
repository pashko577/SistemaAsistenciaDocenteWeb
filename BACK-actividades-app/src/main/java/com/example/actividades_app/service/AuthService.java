package com.example.actividades_app.service;

import com.example.actividades_app.dto.AuthResponseDTO;
import com.example.actividades_app.dto.LoginRequestDTO;
import com.example.actividades_app.dto.RegisterRequestDTO;

public interface AuthService {

    void register(RegisterRequestDTO request);
    AuthResponseDTO login(LoginRequestDTO request);
    
}
