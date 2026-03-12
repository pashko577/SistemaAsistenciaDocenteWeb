package com.example.actividades_app.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Contrato;

@Repository
public interface ContratoRepository extends JpaRepository<Contrato, Long> {

    Optional<Contrato> findByUsuarioId(Long usuarioId);

    boolean existsByUsuarioId(Long usuarioId);

    boolean existsByUsuarioIdAndTipoActividadIdAndEstado(
    Long usuarioId,
    Long tipoActividadId,
    Estado estado
);
}