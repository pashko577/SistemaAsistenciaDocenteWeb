package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.model.Entity.Pago;
import com.example.actividades_app.repository.PagoRepository;
import com.example.actividades_app.service.PagoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;

    @Override
    public Pago crearPago(Pago pago) {

        pago.setId(null); // asegura que sea nuevo
        return pagoRepository.save(pago);
    }

    @Override
    public Pago actualizarPago(Long pagoId, Pago pago) {

        Pago pagoExistente = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pagoExistente.setTipoPago(pago.getTipoPago());
        pagoExistente.setFecha(pago.getFecha());
        pagoExistente.setMontoActividad(pago.getMontoActividad());
        pagoExistente.setNetoPagar(pago.getNetoPagar());
        pagoExistente.setUsuario(pago.getUsuario());

        return pagoRepository.save(pagoExistente);
    }

    @Override
    public void eliminarPago(Long pagoId) {

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pagoRepository.delete(pago);
    }

    @Override
    @Transactional(readOnly = true)
    public Pago obtenerPagoPorId(Long pagoId) {

        return pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarTodos() {

        return pagoRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorUsuario(Long usuarioId) {

        return pagoRepository.findByUsuarioId(usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorFecha(LocalDate fecha) {

        return pagoRepository.findByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {

        return pagoRepository.findByFechaBetween(inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorUsuarioYRangoFechas(Long usuarioId, LocalDate inicio, LocalDate fin) {

        return pagoRepository.findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorTipoPago(Pago.TipoPago tipoPago) {

        return pagoRepository.findByTipoPago(tipoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Pago> listarPorUsuarioYTipoPago(Long usuarioId, Pago.TipoPago tipoPago) {

        return pagoRepository.findByUsuarioIdAndTipoPago(usuarioId, tipoPago);
    }

    @Override
    @Transactional(readOnly = true)
    public Pago obtenerUltimoPago(Long usuarioId) {

        return pagoRepository.findTopByUsuarioIdOrderByFechaDesc(usuarioId)
                .orElseThrow(() -> new RuntimeException("No se encontraron pagos"));
    }
}