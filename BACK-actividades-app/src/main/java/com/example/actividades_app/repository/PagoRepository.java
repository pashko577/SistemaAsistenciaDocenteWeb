package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.Entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

    // =============================
    // CONSULTAS
    // =============================

    // 🔥 usuario viene desde contrato
    List<Pago> findByContratoUsuarioId(Long usuarioId);

    List<Pago> findByFecha(LocalDate fecha);

    List<Pago> findByFechaBetween(
            LocalDate fechaInicio,
            LocalDate fechaFin);

    List<Pago> findByContratoUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    Optional<Pago> findTopByContratoUsuarioIdOrderByFechaDesc(
            Long usuarioId);

    // 🔥 tipoPago ahora pertenece al contrato
    List<Pago> findByContratoTipoPago(
            Contrato.TipoPago tipoPago);

    List<Pago> findByContratoUsuarioIdAndContratoTipoPago(
            Long usuarioId,
            Contrato.TipoPago tipoPago
    );

    // =============================
    // ✅ VALIDACIÓN PLANILLA MENSUAL
    // =============================
    boolean existsByContratoIdAndFechaBetween(
            Long contratoId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );
}