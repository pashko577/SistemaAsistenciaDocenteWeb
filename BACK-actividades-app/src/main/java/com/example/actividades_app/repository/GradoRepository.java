package com.example.actividades_app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Grado;

@Repository
public interface GradoRepository extends JpaRepository<Grado, Long> {

    boolean existsByNumGradoAndNivelId(String numGrado, Long nivelId);
}
