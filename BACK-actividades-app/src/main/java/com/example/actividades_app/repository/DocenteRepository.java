package com.example.actividades_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Docente;

@Repository
public interface DocenteRepository extends JpaRepository<Docente, Long> {

  
    Optional<Docente> findByUsuarioId(Long usuarioId);

    List<Docente> findByEstado(Estado estado);

    List<Docente> findByEspecialidadDocenteId(Long especialidadId);

}
