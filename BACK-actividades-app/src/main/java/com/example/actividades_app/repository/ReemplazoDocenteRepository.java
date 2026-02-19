package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.ReemplazoDocente;

@Repository
public interface ReemplazoDocenteRepository extends JpaRepository<ReemplazoDocente, Long> {

    List<ReemplazoDocente> findByDocenteTitularId(Long docenteId);

    List<ReemplazoDocente> findByDocenteReemplazoId(Long docenteId);

    List<ReemplazoDocente> findByEstado(String estado);
}
