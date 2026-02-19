package com.example.actividades_app.service;

import java.time.LocalDate;
import java.util.List;

import com.example.actividades_app.model.Entity.Pago;

public interface PagoService {

    
    Pago crearPago(Pago pago);

    Pago actualizarPago(Long pagoId, Pago pago);

    void eliminarPago(Long pagoId);

    Pago obtenerPagoPorId(Long pagoId);

    List<Pago> listarTodos();

    List<Pago> listarPorUsuario(Long usuarioId);

    List<Pago> listarPorFecha(LocalDate fecha);

    List<Pago> listarPorRangoFechas(LocalDate inicio, LocalDate fin);

    List<Pago> listarPorUsuarioYRangoFechas(Long usuarioId, LocalDate inicio, LocalDate fin);

    List<Pago> listarPorTipoPago(Pago.TipoPago tipoPago);

    List<Pago> listarPorUsuarioYTipoPago(Long usuarioId, Pago.TipoPago tipoPago);

    Pago obtenerUltimoPago(Long usuarioId);
}
