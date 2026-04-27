package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.HorarioBloque;

@Repository
public interface HorarioBloqueRepository extends JpaRepository<HorarioBloque, Long> {

    boolean existsByOrdenBloqueAndNivelId(Integer ordenBloque, Long nivelId);

    List<HorarioBloque> findAllByOrderByOrdenBloqueAsc();

    List<HorarioBloque> findByNivelIdOrderByOrdenBloqueAsc(Long nivelId);

}