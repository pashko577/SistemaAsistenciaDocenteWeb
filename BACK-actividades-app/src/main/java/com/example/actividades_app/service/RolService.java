package com.example.actividades_app.service;

import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.dto.ModuloUsuario.RegistrarRolRequestDTO;

import java.util.Optional;

import java.util.List;


public interface RolService{
    Rol registrarRol(RegistrarRolRequestDTO dto);
    Optional<Rol> buscarPorNombre(String name);
    List<Rol> obtenerTodosLosRoles();
    void eliminarRol(Long id);
}
