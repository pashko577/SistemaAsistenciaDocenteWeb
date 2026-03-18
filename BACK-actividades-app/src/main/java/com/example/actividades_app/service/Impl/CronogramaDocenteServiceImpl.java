package com.example.actividades_app.service.Impl;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.DiaSemana;
import com.example.actividades_app.model.Entity.AsignacionDocente;
import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.Entity.CronogramaDocente;
import com.example.actividades_app.model.Entity.HorarioBloque;
import com.example.actividades_app.model.Entity.PeriodoAcademico;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.CronogramaDocenteResponseDTO;
import com.example.actividades_app.repository.AsignacionDocenteRepository;
import com.example.actividades_app.repository.CronogramaDiarioRepository;
import com.example.actividades_app.repository.CronogramaDocenteRepository;
import com.example.actividades_app.repository.HorarioBloqueRepository;
import com.example.actividades_app.service.CronogramaDocenteService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CronogramaDocenteServiceImpl implements CronogramaDocenteService {

    private final CronogramaDocenteRepository cronogramaRepository;
    private final AsignacionDocenteRepository asignacionRepository;
    private final HorarioBloqueRepository horarioBloqueRepository;
    private final CronogramaDiarioRepository cronogramaDiarioRepository;

    @Override
    @Transactional
    public CronogramaDocenteResponseDTO crear(CronogramaDocenteRequestDTO dto) {
        // 1. Validaciones de traslape
        if (cronogramaRepository.existsByAsignacionDocenteIdAndDiaSemanaAndHorarioBloqueId(
                dto.getAsignacionDocenteId(), dto.getDiaSemana(), dto.getHorarioBloqueId())) {
            throw new RuntimeException("Ese horario ya está asignado al docente");
        }

        AsignacionDocente asignacion = asignacionRepository.findById(dto.getAsignacionDocenteId())
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        Long docenteId = asignacion.getDocente().getId();
        if (cronogramaRepository.existsByAsignacionDocenteDocenteIdAndDiaSemanaAndHorarioBloqueId(
                docenteId, dto.getDiaSemana(), dto.getHorarioBloqueId())) {
            throw new RuntimeException("El docente ya tiene una clase asignada en ese día y horario");
        }

        HorarioBloque bloque = horarioBloqueRepository.findById(dto.getHorarioBloqueId())
                .orElseThrow(() -> new RuntimeException("Bloque horario no encontrado"));

        // 2. Crear la plantilla del horario (CronogramaDocente)
        CronogramaDocente cronograma = CronogramaDocente.builder()
                .asignacionDocente(asignacion)
                .horarioBloque(bloque)
                .diaSemana(dto.getDiaSemana())
                .build();

        cronogramaRepository.save(cronograma);

        // 3. CAMBIO CLAVE: Generamos solo el mes actual por defecto
        // o puedes llamar a generarCronogramaPeriodo si prefieres seguir con todo el bloque,
        // pero la "limpieza" se hará con finalizarAsignacionDocente.
        generarMesAutomatico(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), asignacion.getId());

        return mapToResponse(cronograma);
    }

    @Override
    @Transactional
    public void generarMesAutomatico(int anio, int mes, Long asignacionId) {
        AsignacionDocente asignacion = asignacionRepository.findById(asignacionId)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        LocalDate inicioMes = LocalDate.of(anio, mes, 1);
        LocalDate finMes = inicioMes.with(java.time.temporal.TemporalAdjusters.lastDayOfMonth());
        
        // Limitar la generación al rango del periodo académico por seguridad
        LocalDate inicioPeriodo = asignacion.getClase().getPeriodoAcademico().getFechaInicio();
        LocalDate finPeriodo = asignacion.getClase().getPeriodoAcademico().getFechaFin();

        LocalDate fechaProceso = inicioMes.isBefore(inicioPeriodo) ? inicioPeriodo : inicioMes;
        LocalDate fechaTope = finMes.isAfter(finPeriodo) ? finPeriodo : finMes;

        List<CronogramaDocente> horariosSemanales = asignacion.getCronogramas();

        while (!fechaProceso.isAfter(fechaTope)) {
            final DayOfWeek dayOfWeekJava = fechaProceso.getDayOfWeek();
            
            for (CronogramaDocente horario : horariosSemanales) {
                if (convertirDia(horario.getDiaSemana()) == dayOfWeekJava) {
                    if (!cronogramaDiarioRepository.existsByCronogramaDocenteIdAndFecha(horario.getId(), fechaProceso)) {
                        CronogramaDiario diario = CronogramaDiario.builder()
                                .cronogramaDocente(horario)
                                .fecha(fechaProceso)
                                .estadoClase(CronogramaDiario.EstadoClase.PROGRAMADA)
                                .build();
                        cronogramaDiarioRepository.save(diario);
                    }
                }
            }
            fechaProceso = fechaProceso.plusDays(1);
        }
    }

    @Override
    @Transactional
    public void finalizarAsignacionDocente(Long asignacionId, LocalDate fechaUltimoDia) {
        // Buscamos las clases generadas que sean posteriores al último día que trabajó
        List<CronogramaDiario> clasesFuturas = cronogramaDiarioRepository
                .findByCronogramaDocenteAsignacionDocenteIdAndFechaAfter(asignacionId, fechaUltimoDia);

        for (CronogramaDiario clase : clasesFuturas) {
            // Solo cancelamos lo que no se ha dictado para no borrar historial real
            if (clase.getEstadoClase() == CronogramaDiario.EstadoClase.PROGRAMADA) {
                clase.setEstadoClase(CronogramaDiario.EstadoClase.CANCELADA);
                clase.setTema("DOCENTE FINALIZÓ LABORES EL " + fechaUltimoDia);
            }
        }
        cronogramaDiarioRepository.saveAll(clasesFuturas);
    }

    // --- MÉTODOS DE APOYO ---

    private DayOfWeek convertirDia(DiaSemana dia) {
        return switch (dia) {
            case LUNES -> DayOfWeek.MONDAY;
            case MARTES -> DayOfWeek.TUESDAY;
            case MIERCOLES -> DayOfWeek.WEDNESDAY;
            case JUEVES -> DayOfWeek.THURSDAY;
            case VIERNES -> DayOfWeek.FRIDAY;
            case SABADO -> DayOfWeek.SATURDAY;
            case DOMINGO -> DayOfWeek.SUNDAY;
        };
    }

    private CronogramaDocenteResponseDTO mapToResponse(CronogramaDocente c) {
        return CronogramaDocenteResponseDTO.builder()
                .id(c.getId())
                .asignacionDocenteId(c.getAsignacionDocente().getId())
                .horarioBloqueId(c.getHorarioBloque().getId())
                .horaInicio(c.getHorarioBloque().getHoraInicio().toString())
                .horaFin(c.getHorarioBloque().getHoraFin().toString())
                .diaSemana(c.getDiaSemana())
                .build();
    }

    @Override
    public List<CronogramaDocenteResponseDTO> listar() {
        return cronogramaRepository.findAll().stream().map(this::mapToResponse).toList();
    }

    @Override
    public CronogramaDocenteResponseDTO obtenerPorId(Long id) {
        return cronogramaRepository.findById(id).map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));
    }

    @Override
    public void eliminar(Long id) {
        cronogramaRepository.deleteById(id);
    }
}