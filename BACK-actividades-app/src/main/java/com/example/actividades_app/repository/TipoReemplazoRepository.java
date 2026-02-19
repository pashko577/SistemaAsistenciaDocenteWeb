package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.TipoReemplazo;

@Repository
public interface TipoReemplazoRepository extends JpaRepository<TipoReemplazo, Long> {
}
