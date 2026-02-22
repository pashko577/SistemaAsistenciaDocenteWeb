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

    List<AsistenciaAdministrativo> findByAdministrativoId(Long AdministrativoId);
    
        Optional<AsistenciaAdministrativo> findByAdministrativoIdAndFecha(Long id, LocalDate fecha);

}
