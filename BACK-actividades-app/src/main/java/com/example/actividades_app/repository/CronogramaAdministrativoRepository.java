package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;

@Repository
public interface CronogramaAdministrativoRepository extends JpaRepository<CronogramaAdministrativo, Long> {

        List<CronogramaAdministrativo> findByAdministrativoId(Long administrativoId);

    List<CronogramaAdministrativo> findByFecha(LocalDate fecha);

    Optional<CronogramaAdministrativo> findByAdministrativoIdAndFecha(Long administrativoId, LocalDate fecha);


    
}
