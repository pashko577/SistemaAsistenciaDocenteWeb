package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.DiaSemana;
import com.example.actividades_app.model.Entity.CronogramaDocente;

@Repository
public interface CronogramaDocenteRepository extends JpaRepository<CronogramaDocente, Long> {

    List<CronogramaDocente> findByAsignacionDocenteId(Long asignacionDocenteId);

    List<CronogramaDocente> findByDiaSemana(DiaSemana diaSemana);

    boolean existsByAsignacionDocenteIdAndDiaSemanaAndHorarioBloqueId(
        Long asignacionDocenteId,
        DiaSemana diaSemana,
        Long horarioBloqueId
);
List<CronogramaDocente> findByAsignacionDocenteDocenteId(Long docenteId);
}