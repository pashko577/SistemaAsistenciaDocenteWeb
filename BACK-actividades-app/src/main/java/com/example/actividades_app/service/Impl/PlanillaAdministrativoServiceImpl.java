package com.example.actividades_app.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.enums.TipoAsistencia;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Pago.PlanillaAdministrativoDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.AsistenciaAdministrativoRepository;
import com.example.actividades_app.repository.ContratoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.PlanillaAdministrativoService;

@Service
@RequiredArgsConstructor
public class PlanillaAdministrativoServiceImpl implements PlanillaAdministrativoService {

    private final AsistenciaAdministrativoRepository asistenciaRepository;
    private final ContratoRepository contratoRepository;
    private final CronogramaAdministrativoRepository cronogramaRepository;
    private final AdministrativoRepository administrativoRepository;

    @Override
    public PlanillaAdministrativoDTO calcularPlanilla(
            Long administrativoId,
            LocalDate inicio,
            LocalDate fin) {

        Administrativo admin = administrativoRepository
                .findById(administrativoId)
                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

        Long usuarioId = admin.getUsuario().getId();

        Contrato contrato = contratoRepository
                .findByUsuarioId(usuarioId)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        BigDecimal sueldoBase = contrato.getMontoBase();

        // --- Fallback si contrato no tiene datos ---
        int diasMes = contrato.getDiasLaborablesMes() != null ? contrato.getDiasLaborablesMes() : 30;
        int horasJornada = contrato.getHorasJornada() != null ? contrato.getHorasJornada() : 8;

        BigDecimal valorDia = sueldoBase.divide(BigDecimal.valueOf(diasMes), 2, RoundingMode.HALF_UP);
        BigDecimal valorHora = valorDia.divide(BigDecimal.valueOf(horasJornada), 2, RoundingMode.HALF_UP);
        BigDecimal valorMinuto = valorHora.divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

        // -------------------------
        // FALTAS
        // -------------------------
        int faltas = asistenciaRepository
                .countByAdministrativo_IdAndTipoAsistenciaAndFechaBetween(
                        administrativoId,
                        TipoAsistencia.FALTA,
                        inicio,
                        fin);

        BigDecimal descFaltas = valorDia.multiply(BigDecimal.valueOf(faltas));

        // -------------------------
        // TARDANZAS
        // -------------------------
        List<AsistenciaAdministrativo> asistencias = asistenciaRepository
                .findByAdministrativo_IdAndFechaBetween(administrativoId, inicio, fin);

        int minutosTardanza = asistencias.stream()
                .filter(a -> a.getTipoAsistencia() == TipoAsistencia.TARDANZA)
                .map(a -> a.getTardanza() == null ? 0 : a.getTardanza())
                .reduce(0, Integer::sum);

        BigDecimal descTardanza = valorMinuto.multiply(BigDecimal.valueOf(minutosTardanza));

        // -------------------------
        // SUELDO NETO
        // -------------------------
        BigDecimal descuentoTotal = descFaltas.add(descTardanza);
        BigDecimal sueldoNeto = sueldoBase.subtract(descuentoTotal);

        return PlanillaAdministrativoDTO.builder()
                .administrativoId(administrativoId)
                .sueldoBase(sueldoBase)
                .faltas(faltas)
                .minutosTardanza(minutosTardanza)
                .descuentoFaltas(descFaltas)
                .descuentoTardanza(descTardanza)
                .sueldoNeto(sueldoNeto)
                .build();
    }

    private int calcularDiasProgramados(Long administrativoId, LocalDate inicio, LocalDate fin) {
        List<CronogramaAdministrativo> cronogramas = cronogramaRepository.findByAdministrativoId(administrativoId);

        int dias = 0;

        for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha = fecha.plusDays(1)) {
            String dia = fecha.getDayOfWeek().name();
            for (CronogramaAdministrativo c : cronogramas) {
                if (c.getDiaSemana().name().equals(dia)) {
                    dias++;
                    break;
                }
            }
        }

        return dias;
    }
}