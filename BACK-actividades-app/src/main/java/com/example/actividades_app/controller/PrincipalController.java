package com.example.actividades_app.controller;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api")
public class PrincipalController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "Acceso admin OK";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String user() {
        return "Acceso user OK";
    }
}
