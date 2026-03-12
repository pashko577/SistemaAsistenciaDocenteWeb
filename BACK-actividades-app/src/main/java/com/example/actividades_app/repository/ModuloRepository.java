package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Modulo;

@Repository
public interface ModuloRepository extends JpaRepository<Modulo, Long> {

    boolean existsByNombre(String nombre);

}