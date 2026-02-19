package com.example.actividades_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.AsistenciaDocente;

@Repository
public interface AsistenciaDocenteRepository extends JpaRepository<AsistenciaDocente, Long> {

 List<AsistenciaDocente> findByDocenteId(Long docenteId);

    Optional<AsistenciaDocente> findByDocenteIdAndCronogramaDiarioId(Long docenteId, Long cronogramaId);
}
