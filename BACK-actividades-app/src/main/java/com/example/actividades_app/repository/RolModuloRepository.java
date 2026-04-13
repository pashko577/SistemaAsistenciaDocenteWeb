package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.RolModulo;

@Repository
public interface RolModuloRepository extends JpaRepository<RolModulo, Long> {

@Query("SELECT rm FROM RolModulo rm JOIN FETCH rm.modulo WHERE rm.rol.id IN :rolIds")
List<RolModulo> findByRolIdIn(@Param("rolIds") List<Long> rolIds);

boolean existsByRolIdAndModuloId(Long rolId, Long moduloId);


    void deleteByRolIdAndModuloId(Long rolId, Long moduloId);
}