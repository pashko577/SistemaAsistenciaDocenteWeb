package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.CronogramaDiario;

@Repository
public interface CronogramaDiarioRepository extends JpaRepository<CronogramaDiario, Long> {

 List<CronogramaDiario> findByClaseId(Long claseId);

    List<CronogramaDiario> findByClaseDocenteId(Long docenteId);

    List<CronogramaDiario> findByFecha(LocalDate fecha);

    List<CronogramaDiario> findByClaseDocenteIdAndFecha(Long docenteId, LocalDate fecha);

    List<CronogramaDiario> findByClaseDocenteIdOrderByFechaAscHoraInicioClaseAsc(Long docenteId);

}
