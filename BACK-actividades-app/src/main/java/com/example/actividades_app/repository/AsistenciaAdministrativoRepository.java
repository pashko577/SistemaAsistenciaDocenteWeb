package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.TipoAsistencia;
import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;

@Repository
public interface AsistenciaAdministrativoRepository extends JpaRepository<AsistenciaAdministrativo, Long> {

    List<AsistenciaAdministrativo> findByAdministrativo_Id(Long administrativoId);

    List<AsistenciaAdministrativo> findByAdministrativo_IdAndFechaBetween(
            Long administrativoId,
            LocalDate inicio,
            LocalDate fin
    );

    boolean existsByAdministrativo_IdAndFecha(
            Long administrativoId,
            LocalDate fecha
    );

    int countByAdministrativo_IdAndFechaBetween(
            Long administrativoId,
            LocalDate inicio,
            LocalDate fin
    );

    int countByAdministrativo_IdAndTipoAsistenciaAndFechaBetween(
            Long administrativoId,
            TipoAsistencia tipoAsistencia,
            LocalDate inicio,
            LocalDate fin
    );
}