package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Persona;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {

      Optional<Persona> findByDni(String dni);

    Optional<Persona> findByEmail(String email);

    boolean existsByDni(String dni);

    boolean existsByEmail(String email);
}
