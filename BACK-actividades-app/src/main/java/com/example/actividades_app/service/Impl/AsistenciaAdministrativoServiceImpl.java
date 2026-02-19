package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.repository.AsistenciaAdministrativoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.AsistenciaAdministrativoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaAdministrativoServiceImpl
        implements AsistenciaAdministrativoService {

    private final AsistenciaAdministrativoRepository repository;
    private final CronogramaAdministrativoRepository cronogramaRepository;

    @Override
    public AsistenciaAdministrativo registrarIngreso(
            Long administrativoId,
            LocalDate fecha) {

        CronogramaAdministrativo cronograma =
                cronogramaRepository
                        .findByAdministrativoIdAndFecha(
                                administrativoId,
                                fecha
                        )
                        .orElseThrow(() ->
                                new RuntimeException("No existe cronograma"));

        LocalTime ahora = LocalTime.now();

        int tardanza = calcularTardanza(
                cronograma.getHoraEntrada(),
                ahora
        );

        AsistenciaAdministrativo asistencia =
                AsistenciaAdministrativo.builder()
                        .administrativo(cronograma.getAdministrativo())
                        .fecha(fecha)
                        .horaIngreso(ahora)
                        .tardanza(tardanza)
                        .build();

        return repository.save(asistencia);
    }

    @Override
    public AsistenciaAdministrativo registrarSalida(
            Long administrativoId,
            LocalDate fecha) {

        AsistenciaAdministrativo asistencia =
                repository.findByAdministrativoIdAndFecha(
                        administrativoId,
                        fecha
                )
                .orElseThrow(() ->
                        new RuntimeException("No hay ingreso registrado"));

        asistencia.setHoraSalida(LocalTime.now());

        return repository.save(asistencia);
    }

    @Override
    public List<AsistenciaAdministrativo> obtenerPorAdministrativo(
            Long administrativoId) {

        return repository.findByAdministrativoId(administrativoId);
    }

    @Override
    public List<AsistenciaAdministrativo> obtenerPorAdministrativoYMes(
            Long administrativoId,
            int mes,
            int anio) {

        return repository.findByAdministrativoIdAndMes(
                administrativoId,
                mes,
                anio
        );
    }

    // =====================================================
    // REGLA NEGOCIO: calcular tardanza
    // =====================================================

    private int calcularTardanza(
            LocalTime horaEntrada,
            LocalTime horaReal) {

        if (horaReal.isAfter(horaEntrada)) {

            return (int) java.time.Duration
                    .between(horaEntrada, horaReal)
                    .toMinutes();
        }

        return 0;
    }

}

