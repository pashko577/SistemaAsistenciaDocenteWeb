package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Seccion;

@Repository
public interface SeccionRepository extends JpaRepository<Seccion, Long> {

    Optional<Seccion> findByNomSeccion(String nomSeccion);
}
