package com.example.actividades_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.actividades_app.model.Entity.Rol;

import java.util.List;


public interface RolRepository extends JpaRepository<Rol, Long> {
    //buscar rol por nombre
    List<Rol> findByNombreRol(String nombreRol);
    //verificar si un rol existe por su nombre
    boolean existsByNombreRol(String nombreRol);
}
