package com.example.actividades_app.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;
import com.example.actividades_app.model.Entity.AsistenciaDocente;
import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.Entity.DetalleReporteDocente;
import com.example.actividades_app.model.Entity.Docente;
import com.example.actividades_app.model.Entity.Contrato.TipoPago;
import com.example.actividades_app.model.dto.ModuloDocente.PlanillaDocenteDTO;
import com.example.actividades_app.repository.AsistenciaDocenteRepository;
import com.example.actividades_app.repository.ContratoRepository;
import com.example.actividades_app.repository.DetalleReporteDocenteRepository;
import com.example.actividades_app.repository.DocenteRepository;
import com.example.actividades_app.service.PlanillaDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanillaDocenteServiceImpl implements PlanillaDocenteService {

    private final DocenteRepository docenteRepository;
    private final ContratoRepository contratoRepository;
    private final AsistenciaDocenteRepository asistenciaRepository;
    private final DetalleReporteDocenteRepository detalleReporteRepository;

    @Override
    public PlanillaDocenteDTO calcularPlanilla(Long docenteId, LocalDate inicio, LocalDate fin) {

        // -----------------------------
        // DOCENTE
        // -----------------------------
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        Long usuarioId = docente.getUsuario().getId();

        // -----------------------------
        // CONTRATO
        // -----------------------------
        Contrato contrato = contratoRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
        TipoPago tipoPago = contrato.getTipoPago();
        BigDecimal sueldoBase = contrato.getMontoBase();
        BigDecimal valorHora = sueldoBase;

        // -----------------------------
        // ASISTENCIAS
        // -----------------------------
        List<AsistenciaDocente> asistencias = asistenciaRepository
                .findByCronogramaDiario_CronogramaDocente_AsignacionDocente_Docente_IdAndCronogramaDiario_FechaBetween(
                        docenteId, inicio, fin);

        int faltas = (int) asistencias.stream()
                .filter(a -> a.getEstadoAsistencia() == EstadoAsistenciaDocente.FALTA)
                .count();

        int horasDictadas = (int) asistencias.stream()
                .filter(a -> a.getEstadoAsistencia() == EstadoAsistenciaDocente.PUNTUAL
                        || a.getEstadoAsistencia() == EstadoAsistenciaDocente.TARDANZA) // Considerar tardanza como hora dictada
                .count();

        int minutosTardanza = asistencias.stream()
                .map(this::calcularMinutosTardanza)
                .reduce(0, Integer::sum);

        int registros = asistencias.size();
        int minutosAjustados = Math.max(0, minutosTardanza - (registros * 3));

        BigDecimal descTardanza = calcularDescuentoTardanza(minutosAjustados, valorHora);
        BigDecimal descFaltas = calcularDescuentoFaltas(faltas, valorHora);

        // -----------------------------
        // CRITERIOS ACADEMICOS
        // -----------------------------
        List<DetalleReporteDocente> detalles = detalleReporteRepository
                .findByReporteDocente_CronogramaDiario_CronogramaDocente_AsignacionDocente_Docente_IdAndReporteDocente_CronogramaDiario_FechaBetween(
                        docenteId, inicio, fin);

        int criterio1SI = 0, criterio1REG = 0, criterio1NO = 0;
        int criterio2SI = 0, criterio2REG = 0, criterio2NO = 0;

        for (DetalleReporteDocente d : detalles) {

            // CRITERIO 1
            if (d.getPubliWSS() == null) {
                criterio1REG++;
            } else if (Boolean.TRUE.equals(d.getPubliWSS())) {
                criterio1SI++;
            } else {
                criterio1NO++;
            }

            // CRITERIO 2
            if (d.getAgendaE() == null) {
                criterio2REG++;
            } else if (Boolean.TRUE.equals(d.getAgendaE())) {
                criterio2SI++;
            } else {
                criterio2NO++;
            }
        }

        BigDecimal descCriterios = calcularDescuentoCriterios(criterio1SI, criterio1REG, criterio1NO,
                criterio2SI, criterio2REG, criterio2NO, valorHora);

        // -----------------------------
        // SUELDO BRUTO
        // -----------------------------
        BigDecimal sueldoBruto = tipoPago == TipoPago.PAGO_HORA
                ? valorHora.multiply(BigDecimal.valueOf(horasDictadas))
                : sueldoBase;

        // -----------------------------
        // DESCUENTO TOTAL
        // -----------------------------
        BigDecimal descuentoTotal = descFaltas.add(descTardanza).add(descCriterios)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal sueldoNeto = sueldoBruto.subtract(descuentoTotal).setScale(2, RoundingMode.HALF_UP);

        // -----------------------------
        // DTO
        // -----------------------------
        return PlanillaDocenteDTO.builder()
                .docenteId(docenteId)
                .tipoPago(tipoPago)
                .sueldoBase(sueldoBase.setScale(2, RoundingMode.HALF_UP))
                .horasDictadas(horasDictadas)
                .faltas(faltas)
                .minutosTardanza(minutosTardanza)

                .criterio1SI(criterio1SI)
                .criterio1REG(criterio1REG)
                .criterio1NO(criterio1NO)

                .criterio2SI(criterio2SI)
                .criterio2REG(criterio2REG)
                .criterio2NO(criterio2NO)

                .descuentoFaltas(descFaltas)
                .descuentoTardanza(descTardanza)
                .descuentoCriterios(descCriterios)

                .sueldoBruto(sueldoBruto)
                .sueldoNeto(sueldoNeto)
                .build();
    }

    // ==============================
    // Métodos privados
    // ==============================

    private Integer calcularMinutosTardanza(AsistenciaDocente asistencia) {
        if (asistencia.getHoraEntradaDoc() == null
                || asistencia.getCronogramaDiario() == null
                || asistencia.getCronogramaDiario().getCronogramaDocente() == null
                || asistencia.getCronogramaDiario().getCronogramaDocente().getHorarioBloque() == null
                || asistencia.getCronogramaDiario().getCronogramaDocente().getHorarioBloque().getHoraInicio() == null) {
            return 0;
        }

        LocalTime horaInicio = asistencia.getCronogramaDiario().getCronogramaDocente().getHorarioBloque().getHoraInicio();
        long minutos = ChronoUnit.MINUTES.between(horaInicio, asistencia.getHoraEntradaDoc());
        return minutos > 0 ? (int) minutos : 0;
    }

    private BigDecimal calcularDescuentoTardanza(int minutosAjustados, BigDecimal valorHora) {
        if (minutosAjustados < 11) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.valueOf(minutosAjustados)
                .divide(BigDecimal.valueOf(45), 4, RoundingMode.HALF_UP)
                .multiply(valorHora)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularDescuentoFaltas(int faltas, BigDecimal valorHora) {
        return valorHora.multiply(BigDecimal.valueOf(0.5))
                .multiply(BigDecimal.valueOf(faltas))
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal calcularDescuentoCriterios(int c1SI, int c1REG, int c1NO,
                                                   int c2SI, int c2REG, int c2NO,
                                                   BigDecimal valorHora) {

        double puntaje = (c1REG * 0.3) + (c1NO * 0.5) + (c2REG * 0.3) + (c2NO * 0.5);

        // regla especial
        if (puntaje >= 11) {
            puntaje = (c1REG * 0.1) + (c1NO * 0.5) + (c2REG * 0.1) + (c2NO * 0.5);
        }

        if (puntaje < 11) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.valueOf(puntaje).multiply(valorHora).setScale(2, RoundingMode.HALF_UP);
    }
}