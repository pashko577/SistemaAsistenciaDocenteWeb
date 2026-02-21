package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.actividades_app.model.Entity.Usuario;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u " +
           "JOIN FETCH u.persona " +
           "JOIN FETCH u.roles " +
           "WHERE u.persona.dni = :dni")
    Optional<Usuario> findByPersonaDni(@Param("dni") String dni);

    boolean existsByPersonaDni(String dni);
}