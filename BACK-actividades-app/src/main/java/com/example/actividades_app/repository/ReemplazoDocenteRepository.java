package com.example.actividades_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.EstadoReemplazo;
import com.example.actividades_app.model.Entity.ReemplazoDocente;

@Repository
public interface ReemplazoDocenteRepository extends JpaRepository<ReemplazoDocente, Long> {

    List<ReemplazoDocente> findByDocenteTitularId(Long docenteTitularId);

    List<ReemplazoDocente> findByDocenteReemplazoId(Long docenteReemplazoId);

    List<ReemplazoDocente> findByEstado(EstadoReemplazo estado);

    Optional<ReemplazoDocente> findByCronogramaDiarioId(Long cronogramaDiarioId);

    boolean existsByCronogramaDiarioId(Long cronogramaDiarioId);

}