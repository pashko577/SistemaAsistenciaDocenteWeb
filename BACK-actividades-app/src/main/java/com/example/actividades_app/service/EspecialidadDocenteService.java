package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;

public interface EspecialidadDocenteService {
    EspecialidadDocente crearEspecialidadDocente(EspecialidadDocenteRequestDTO dto);

    EspecialidadDocente actualizarEspecialidadDocente(Long especialidadDocenteID, String nombreEspecialidad);

    void eliminarEspecilidadDocente(Long especialidadDocenteID);

    EspecialidadDocente obtenerPorId(Long especialidadDocenteID);

    List<EspecialidadDocente> listarEspecialidadDocente();
}
