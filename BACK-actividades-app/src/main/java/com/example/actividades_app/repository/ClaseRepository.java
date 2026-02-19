package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

   List<Clase> findByDocenteId(Long docenteId);

    List<Clase> findByCursoId(Long cursoId);

    List<Clase> findByGradoId(Long gradoId);

    List<Clase> findBySeccionId(Long seccionId);

    List<Clase> findByNivelId(Long nivelId);

    List<Clase> findByDocenteIdAndNivelId(Long docenteId, Long nivelId);

}
