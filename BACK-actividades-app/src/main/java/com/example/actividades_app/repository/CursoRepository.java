package com.example.actividades_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

      Optional<Curso> findByNombreCurso(String nombreCurso);

    boolean existsByNombreCurso(String nombreCurso);
}
