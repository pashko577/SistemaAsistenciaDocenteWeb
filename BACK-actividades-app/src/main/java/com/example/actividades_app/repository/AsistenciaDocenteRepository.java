package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.AsistenciaDocente;

@Repository
public interface AsistenciaDocenteRepository extends JpaRepository<AsistenciaDocente, Long> {

    Optional<AsistenciaDocente> findByCronogramaDiarioId(Long cronogramaDiarioId);

    boolean existsByCronogramaDiarioId(Long cronogramaDiarioId);

      List<AsistenciaDocente> findByCronogramaDiario_CronogramaDocente_AsignacionDocente_Docente_IdAndCronogramaDiario_FechaBetween(
        Long docenteId, LocalDate inicio, LocalDate fin);
}