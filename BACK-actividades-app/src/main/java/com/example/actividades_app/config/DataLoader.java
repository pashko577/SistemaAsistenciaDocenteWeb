package com.example.actividades_app.config;

import com.example.actividades_app.model.Rol;
import com.example.actividades_app.repository.RolRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public void run(String... args) {

        if (!rolRepository.existsByName("USER")) {
            rolRepository.save(new Rol(null, "USER"));
        }

        if (!rolRepository.existsByName("ADMIN")) {
            rolRepository.save(new Rol(null, "ADMIN"));
        }
    }
}

