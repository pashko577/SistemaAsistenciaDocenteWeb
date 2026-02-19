package com.example.actividades_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Administrativo;

@Repository
public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {

    Optional<Administrativo> findByUsuarioId(Long usuarioId);

    List<Administrativo> findByEstado(Estado estado);

}
