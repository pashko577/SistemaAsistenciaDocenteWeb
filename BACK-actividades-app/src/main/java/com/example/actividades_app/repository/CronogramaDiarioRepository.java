package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDiariaDTO;

@Repository
public interface CronogramaDiarioRepository extends JpaRepository<CronogramaDiario, Long> {

    List<CronogramaDiario> findByFecha(LocalDate fecha);

    List<CronogramaDiario> findByCronogramaDocenteId(Long cronogramaDocenteId);

    boolean existsByCronogramaDocenteIdAndFecha(
            Long cronogramaDocenteId,
            LocalDate fecha);

    List<CronogramaDiario> findByCronogramaDocenteAsignacionDocenteIdAndFechaAfter(Long asignacionId, LocalDate fecha);

    @Query("SELECT new com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDiariaDTO(" +
            "cd.id, hb.horaInicio, hb.horaFin, " +
            "concat(p.nombres, ' ', p.apellidos), cu.nombreCurso, " +
            "concat(g.numGrado, ' ', s.nomSeccion), n.nomNivel, " +
            "cl.aula, cast(cd.estadoClase as string), cd.tema, " +
            "ad.horaEntradaDoc, ad.minutosTardanza, cast(ad.estadoAsistencia as string)) " +
            "FROM CronogramaDiario cd " +
            "JOIN cd.cronogramaDocente cdoc " +
            "JOIN cdoc.horarioBloque hb " +
            "JOIN cdoc.asignacionDocente asig " +
            "JOIN asig.docente d " +
            "JOIN d.usuario u " +
            "JOIN u.persona p " +
            "JOIN asig.clase cl " +
            "JOIN cl.curso cu " +
            "JOIN cl.seccion s " +
            "JOIN s.grado g " +
            "JOIN g.nivel n " +
            "LEFT JOIN AsistenciaDocente ad ON ad.cronogramaDiario.id = cd.id " +
            "WHERE cd.fecha = :fecha " +
            "ORDER BY n.nomNivel ASC, g.numGrado ASC, hb.horaInicio ASC")
    List<AsistenciaDiariaDTO> obtenerControlDiarioPorFecha(@Param("fecha") LocalDate fecha);

}