package com.example.actividades_app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Curso;

@Repository
public interface CursoRepository extends JpaRepository<Curso, Long> {

  boolean existsByNombreCurso(String nombreCurso);
}
