package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Auditoria;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {


    List<Auditoria> findByUsuarioId(Long usuarioId);

    List<Auditoria> findByTablaAfectada(String tabla);

}
