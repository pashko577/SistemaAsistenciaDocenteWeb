package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.actividades_app.model.dto.ModuloUsuario.AuthResponseDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.LoginRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.RegisterRequestDTO;
import com.example.actividades_app.service.AuthService;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDTO request) {
        authService.register(request);        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        return ResponseEntity.ok(authService.login(request));
    }
    
}
