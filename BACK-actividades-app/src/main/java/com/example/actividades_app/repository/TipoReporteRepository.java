package com.example.actividades_app.repository;





import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.TipoReporte;

@Repository
public interface TipoReporteRepository extends JpaRepository<TipoReporte, Long> {

    boolean existsByNombreTipoReporte(String nombreTipoReporte);

}