package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.ReporteDocente;

@Repository
public interface ReporteDocenteRepository extends JpaRepository<ReporteDocente, Long> {

   List<ReporteDocente> findByDocenteId(Long docenteId);

    List<ReporteDocente> findByFecha(LocalDate fecha);

    Optional<ReporteDocente> findByDocenteIdAndCronogramaDiarioId(Long docenteId, Long cronogramaId);

}

