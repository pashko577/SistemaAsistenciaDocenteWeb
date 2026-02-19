package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.Sede;

public interface SedeService {
    Sede crearSede(String nombre);

    Sede actualizarSede(Long sedeId, String nombre);

    void eliminarSede(Long sedeId);

    Sede obtenerPorId(Long sedeId);

    List<Sede> listarTodas();
}
