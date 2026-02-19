package com.example.actividades_app.service;

import java.util.List;
import java.util.Optional;

import com.example.actividades_app.model.Entity.Administrativo;

public interface AdministrativoService {
    Administrativo crear(Administrativo administrativo);

    Administrativo actualizar(Long id, Administrativo administrativo);

    Optional<Administrativo> obtenerPorId(Long id);

    List<Administrativo> listarTodos();

    List<Administrativo> listarActivos();

    void eliminar(Long id);
}
