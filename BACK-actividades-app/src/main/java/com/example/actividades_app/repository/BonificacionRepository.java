package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Bonificacion;

@Repository
public interface BonificacionRepository extends JpaRepository<Bonificacion, Long> {

        List<Bonificacion> findByPagoId(Long pagoId);

}

