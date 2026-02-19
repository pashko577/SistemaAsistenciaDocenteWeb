package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.CronogramaAdministrativoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CronogramaAdministrativoServiceImpl implements CronogramaAdministrativoService {

    private final CronogramaAdministrativoRepository repository;

    @Override
    public CronogramaAdministrativo crear(CronogramaAdministrativo cronograma) {

        return repository.save(cronograma);
    }

    @Override
    public List<CronogramaAdministrativo> obtenerPorAdministrativo(Long administrativoId) {

        return repository.findByAdministrativoId(administrativoId);
    }

    @Override
    public CronogramaAdministrativo obtenerPorAdministrativoYFecha(Long administrativoId, LocalDate fecha) {
        return repository.findByAdministrativoIdAndFecha(administrativoId, fecha)
                .orElseThrow(() -> new RuntimeException("No existe cronograma"));
    }

}
