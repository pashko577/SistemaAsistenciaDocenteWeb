package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

      List<Pago> findByUsuarioId(Long usuarioId);

    List<Pago> findByFecha(LocalDate fecha);
}
