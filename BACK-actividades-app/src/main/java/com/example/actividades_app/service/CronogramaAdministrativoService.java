package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.Entity.CronogramaAdministrativo;

public interface CronogramaAdministrativoService {

        CronogramaAdministrativo crear(CronogramaAdministrativo cronograma);

    List<CronogramaAdministrativo> obtenerPorAdministrativo(Long administrativoId);

    CronogramaAdministrativo obtenerPorAdministrativoYFecha(
            Long administrativoId,
            LocalDate fecha
    );

}
