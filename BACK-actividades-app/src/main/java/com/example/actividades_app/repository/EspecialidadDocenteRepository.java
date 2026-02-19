package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.EspecialidadDocente;

@Repository
public interface EspecialidadDocenteRepository extends JpaRepository<EspecialidadDocente, Long> {

        Optional<EspecialidadDocente> findByNombreEspecialidad(String nombreEspecialidad);

        
}
