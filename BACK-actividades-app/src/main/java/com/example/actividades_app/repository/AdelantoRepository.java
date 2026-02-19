package com.example.actividades_app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Adelanto;

@Repository
public interface AdelantoRepository extends JpaRepository<Adelanto, Long> {

      List<Adelanto> findByPagoId(Long pagoId);
}
