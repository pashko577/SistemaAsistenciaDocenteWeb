package com.example.actividades_app.repository;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.TipoReporte;

@Repository
public interface TipoReporteRepository extends JpaRepository<TipoReporte, Long> {

    // Buscar por nombre (ESCOLAR, ACADEMIA, TUTORIA)
    Optional<TipoReporte> findByNombreTipoReporte(String nombreTipoReporte);

    // Validar si ya existe un tipo
    boolean existsByNombreTipoReporte(String nombreTipoReporte);
}