package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.actividades_app.model.Usuario;
import java.util.Optional;


public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Buscar usuario por su nombre de usuario (para autenticación)
    Optional<Usuario> findByUsername(String username);

    //Verificar si un usuario existe por su nombre de usuario
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
