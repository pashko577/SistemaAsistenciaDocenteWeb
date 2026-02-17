package com.example.actividades_app.service.Impl;

import com.example.actividades_app.service.RolService;
import com.example.actividades_app.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.actividades_app.dto.RegistrarRolRequestDTO;
import com.example.actividades_app.model.Entity.Rol;

import org.springframework.stereotype.Service;

import java.util.Optional;

import com.example.actividades_app.config.exception.RoleNotFoundException;
import com.example.actividades_app.config.exception.RoleAlreadyExistsException;


import java.util.List;

@Service
public class RolServiceImpl implements RolService {

    @Autowired
    private RolRepository rolRepository;

    @Override
    public Rol registrarRol(RegistrarRolRequestDTO dto) {

        // Regla: evitar duplicados
        if (rolRepository.existsByName(dto.getName())) {
            throw new RoleAlreadyExistsException("El rol ya existe");
        }

        // Crear entidad
        Rol rol = new Rol();
        rol.setNombreRol(dto.getName());

        // Guardar
        return rolRepository.save(rol);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String name) {
        return rolRepository.findByName(name);
    }

    @Override
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    public void eliminarRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("El rol con ID" + id + " no existe"));

        rolRepository.delete(rol);
    }

}
