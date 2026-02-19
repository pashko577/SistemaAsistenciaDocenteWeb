package com.example.actividades_app.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.actividades_app.model.Entity.Pago;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {

     // Buscar pagos por usuario
    List<Pago> findByUsuarioId(Long usuarioId);

    // Buscar pagos por fecha exacta
    List<Pago> findByFecha(LocalDate fecha);

    // Buscar pagos entre fechas (para reportes mensuales)
    List<Pago> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    // Buscar pagos por usuario y rango de fechas (MUY IMPORTANTE)
    List<Pago> findByUsuarioIdAndFechaBetween(
            Long usuarioId,
            LocalDate fechaInicio,
            LocalDate fechaFin
    );

    // Buscar último pago de un usuario
    Optional<Pago> findTopByUsuarioIdOrderByFechaDesc(Long usuarioId);

    // Buscar pagos por tipo (POR_HORA o MENSUAL)
    List<Pago> findByTipoPago(Pago.TipoPago tipoPago);

    // Buscar pagos por usuario y tipo
    List<Pago> findByUsuarioIdAndTipoPago(
            Long usuarioId,
            Pago.TipoPago tipoPago
    );
}
