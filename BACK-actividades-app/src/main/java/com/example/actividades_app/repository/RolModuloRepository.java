package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.RolModulo;

@Repository
public interface RolModuloRepository extends JpaRepository<RolModulo, Long> {

    List<RolModulo> findByRolId(Long rolId);

    boolean existsByRolIdAndModuloId(Long rolId, Long moduloId);


    void deleteByRolIdAndModuloId(Long rolId, Long moduloId);
}