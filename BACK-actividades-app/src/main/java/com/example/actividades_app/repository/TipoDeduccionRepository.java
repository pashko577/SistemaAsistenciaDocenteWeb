package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.TipoDeduccion;

@Repository
public interface TipoDeduccionRepository extends JpaRepository<TipoDeduccion, Long> {

      Optional<TipoDeduccion> findByNombre(String nombre);

    boolean existsByNombre(String nombre);
}
