package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;

@Repository
public interface AsistenciaAdministrativoRepository extends JpaRepository<AsistenciaAdministrativo, Long> {

        List<AsistenciaAdministrativo> findByAdministrativoId(Long administrativoId);

        Optional<AsistenciaAdministrativo> findByAdministrativoIdAndFecha(Long id, LocalDate fecha);


        @Query("SELECT a FROM AsistenciaAdministrativo a " +
       "WHERE a.administrativo.administrativoID = :administrativoId " +
       "AND MONTH(a.fecha) = :mes " +
       "AND YEAR(a.fecha) = :anio")
List<AsistenciaAdministrativo> findByAdministrativoIdAndMes(
        @Param("administrativoId") Long administrativoId,
        @Param("mes") int mes,
        @Param("anio") int anio); 


}
