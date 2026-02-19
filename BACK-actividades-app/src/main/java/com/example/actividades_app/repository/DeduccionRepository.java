package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Deduccion;

@Repository
public interface DeduccionRepository extends JpaRepository<Deduccion, Long> {

        List<Deduccion> findByPagoId(Long pagoId);

}
