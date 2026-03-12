package com.example.actividades_app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Clase;

@Repository
public interface ClaseRepository extends JpaRepository<Clase, Long> {

    boolean existsByCursoIdAndSeccionIdAndPeriodoAcademicoId(
            Long cursoId,
            Long seccionId,
            Long periodoAcademicoId
    );

}
