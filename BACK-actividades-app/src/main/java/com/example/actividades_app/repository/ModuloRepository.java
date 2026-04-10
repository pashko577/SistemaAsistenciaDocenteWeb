package com.example.actividades_app.repository;

import java.util.Optional; // Importante
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.actividades_app.model.Entity.Modulo;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {
    
    // Cambiamos esto para poder obtener el objeto y comparar su ruta
    Optional<Modulo> findByNombre(String nombre);
    
    boolean existsByNombre(String nombre);
}