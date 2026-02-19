package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.actividades_app.model.Entity.Usuario;

import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Buscar usuario por su nombre de usuario (para autenticación)
    //Optional<Usuario> findByUsername(String username);

    //Verificar si un usuario existe por su nombre de usuario
    //boolean existsByUsername(String username);

    // CORRECCIÓN
    @Query("SELECT u FROM Usuario u WHERE u.persona.dni = :dni") 
    Optional<Usuario> findByDni(@Param("dni") String dni);

    boolean existsByPersonaDni(String dni);
}
