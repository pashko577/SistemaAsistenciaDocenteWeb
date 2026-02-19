package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.TipoAsistencia;

@Repository
public interface TipoAsistenciaRepository extends JpaRepository<TipoAsistencia, Long> {
}