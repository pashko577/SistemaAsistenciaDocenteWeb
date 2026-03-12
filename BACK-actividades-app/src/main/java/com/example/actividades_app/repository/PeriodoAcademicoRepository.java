package com.example.actividades_app.repository;

import com.example.actividades_app.model.Entity.PeriodoAcademico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PeriodoAcademicoRepository extends JpaRepository<PeriodoAcademico, Long> {

    boolean existsByNombre(String nombre);

}