package com.example.actividades_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Administrativo;

@Repository
public interface AdministrativoRepository extends JpaRepository<Administrativo, Long> {

    @Query("SELECT a FROM Administrativo a WHERE EXISTS " +
            "(SELECT c FROM Contrato c WHERE c.usuario.id = a.usuario.id AND c.estado = 'ACTIVO')")
    List<Administrativo> findByHasActiveContrato();

    @Query("SELECT a FROM Administrativo a WHERE a.usuario.id = :usuarioId")
    Optional<Administrativo> findByUsuarioId(@Param("usuarioId") Long usuarioId);

    List<Administrativo> findByEstado(Estado estado);

    List<Administrativo> findByEstadoNot(Estado estado);

}
