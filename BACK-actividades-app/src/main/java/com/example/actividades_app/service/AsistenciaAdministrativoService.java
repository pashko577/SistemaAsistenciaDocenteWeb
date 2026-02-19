package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;

public interface AsistenciaAdministrativoService {

    AsistenciaAdministrativo registrarIngreso(
            Long administrativoId,
            LocalDate fecha
    );

    AsistenciaAdministrativo registrarSalida(
            Long administrativoId,
            LocalDate fecha
    );

    List<AsistenciaAdministrativo> obtenerPorAdministrativo(
            Long administrativoId
    );

    List<AsistenciaAdministrativo> obtenerPorAdministrativoYMes(
            Long administrativoId,
            int mes,
            int anio
    );

}
