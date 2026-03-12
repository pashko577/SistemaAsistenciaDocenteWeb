package com.example.actividades_app.repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.ReporteDocente;

@Repository
public interface ReporteDocenteRepository extends JpaRepository<ReporteDocente, Long> {

    boolean existsByCronogramaDiarioId(Long cronogramaDiarioId);

    List<ReporteDocente> findByCronogramaDiarioId(Long cronogramaDiarioId);

}