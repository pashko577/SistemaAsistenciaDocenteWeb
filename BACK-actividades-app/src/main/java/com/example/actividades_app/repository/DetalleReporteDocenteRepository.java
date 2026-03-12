package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.DetalleReporteDocente;

@Repository
public interface DetalleReporteDocenteRepository extends JpaRepository<DetalleReporteDocente, Long> {

    Optional<DetalleReporteDocente> findByReporteDocenteId(Long reporteDocenteId);

    List<DetalleReporteDocente> findByReporteDocente_CronogramaDiario_CronogramaDocente_AsignacionDocente_Docente_IdAndReporteDocente_CronogramaDiario_FechaBetween(
            Long docenteId, LocalDate inicio, LocalDate fin);
}


