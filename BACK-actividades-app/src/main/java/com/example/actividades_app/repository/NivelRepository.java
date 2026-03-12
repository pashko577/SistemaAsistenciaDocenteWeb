package com.example.actividades_app.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Nivel;

@Repository
public interface NivelRepository extends JpaRepository<Nivel, Long> {
    
    boolean existsByNomNivel(String nomNivel);
}
