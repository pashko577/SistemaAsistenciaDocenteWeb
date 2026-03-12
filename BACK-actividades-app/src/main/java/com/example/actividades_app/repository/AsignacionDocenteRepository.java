package com.example.actividades_app.repository;

import com.example.actividades_app.model.Entity.AsignacionDocente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsignacionDocenteRepository extends JpaRepository<AsignacionDocente, Long> {
}