package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.dto.ModuloUsuario.SedeRequestDTO;

public interface SedeService {
    Sede crearSede(SedeRequestDTO dto);

    Sede actualizarSede(Long sedeId, String nombre);

    void eliminarSede(Long sedeId);

    Sede obtenerPorId(Long sedeId);

    List<Sede> listarTodas();
}
