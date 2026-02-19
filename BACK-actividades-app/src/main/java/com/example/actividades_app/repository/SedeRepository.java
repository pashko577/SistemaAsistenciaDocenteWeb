package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.actividades_app.model.Entity.Sede;

public interface SedeRepository extends JpaRepository<Sede,Long> {

    Optional<Sede> findByNombreSede(String nombreSede);

    boolean existsByNombreSede(String nombreSede);
}
