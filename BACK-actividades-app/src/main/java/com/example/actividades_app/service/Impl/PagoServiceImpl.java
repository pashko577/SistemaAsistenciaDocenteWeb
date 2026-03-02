package com.example.actividades_app.service.Impl;

import com.example.actividades_app.model.Entity.*;
import com.example.actividades_app.model.dto.Pago.PagoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.BonificacionRequestDTO;
import com.example.actividades_app.model.dto.Reporte.DescuentoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;
import com.example.actividades_app.repository.*;
import com.example.actividades_app.service.PagoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

    private final PagoRepository pagoRepository;
    private final ContratoRepository contratoRepository;
    private final AdelantoRepository adelantoRepository;
    private final BonificacionRepository bonificacionRepository;
    private final DeduccionRepository deduccionRepository;
    private final TipoDeduccionRepository tipoDeduccionRepository;
    private final AdministrativoRepository administrativoRepository;
    private final DocenteRepository docenteRepository;

    // =====================================================
    // CREAR PAGO
    // =====================================================
    @Override
    public PagoResponseDTO crear(PagoRequestDTO dto) {

        Contrato contrato = contratoRepository.findById(dto.getContratoId())
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        // ✅ calcular monto según tipo pago
        BigDecimal montoActividad = calcularMontoActividad(
                contrato,
                dto.getHorasTrabajadas());

        Pago pago = Pago.builder()
                .fecha(dto.getFecha())
                .contrato(contrato)
                .montoActividad(montoActividad)
                .build();

        pago = pagoRepository.save(pago);

        BigDecimal totalBonificaciones = BigDecimal.ZERO;
        BigDecimal totalDeducciones = BigDecimal.ZERO;
        BigDecimal totalAdelantos = BigDecimal.ZERO;

        // ================= ADELANTOS =================
        if (dto.getAdelantos() != null) {
            for (AdelantoRequestDTO a : dto.getAdelantos()) {

                Adelanto adelanto = Adelanto.builder()
                        .nombre(a.getNombre())
                        .monto(a.getMonto())
                        .pago(pago)
                        .build();

                adelantoRepository.save(adelanto);
                totalAdelantos = totalAdelantos.add(a.getMonto());
            }
        }

        // ================= BONIFICACIONES =================
        if (dto.getBonificaciones() != null) {
            for (BonificacionRequestDTO b : dto.getBonificaciones()) {

                Bonificacion bonificacion = Bonificacion.builder()
                        .nombre(b.getNombre())
                        .monto(b.getMonto())
                        .pago(pago)
                        .build();

                bonificacionRepository.save(bonificacion);
                totalBonificaciones = totalBonificaciones.add(b.getMonto());
            }
        }

        // ================= DEDUCCIONES =================
        if (dto.getDeducciones() != null) {
            for (DescuentoRequestDTO d : dto.getDeducciones()) {

                TipoDeduccion tipo = tipoDeduccionRepository
                        .findById(d.getTipoDeduccionId())
                        .orElseThrow(() -> new RuntimeException("Tipo deducción no encontrado"));

                Deduccion deduccion = Deduccion.builder()
                        .tipoDeduccion(tipo)
                        .observaciones(d.getObservaciones())
                        .monto(d.getMonto())
                        .pago(pago)
                        .build();

                deduccionRepository.save(deduccion);
                totalDeducciones = totalDeducciones.add(d.getMonto());
            }
        }

        // ================= NETO =================
        BigDecimal neto = montoActividad
                .add(totalBonificaciones)
                .subtract(totalDeducciones.add(totalAdelantos));

        pago.setNetoPagar(neto);

        return mapToResponse(pago);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================
    @Override
    public PagoResponseDTO actualizar(Long id, PagoRequestDTO dto) {

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        pago.setFecha(dto.getFecha());

        return mapToResponse(pagoRepository.save(pago));
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @Override
    public void eliminar(Long id) {
        pagoRepository.deleteById(id);
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================
    @Override
    public PagoResponseDTO buscarPorId(Long id) {
        return mapToResponse(
                pagoRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Pago no encontrado")));
    }

    // =====================================================
    // CONSULTAS
    // =====================================================
    @Override
    public List<PagoResponseDTO> listarPorUsuario(Long usuarioId) {
        return pagoRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PagoResponseDTO> listarPorFecha(LocalDate fecha) {
        return pagoRepository.findByFecha(fecha)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PagoResponseDTO> listarPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return pagoRepository.findByFechaBetween(inicio, fin)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<PagoResponseDTO> listarPorUsuarioYRangoFechas(
            Long usuarioId,
            LocalDate inicio,
            LocalDate fin) {

        return pagoRepository
                .findByUsuarioIdAndFechaBetween(usuarioId, inicio, fin)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public PagoResponseDTO obtenerUltimoPago(Long usuarioId) {

        Pago pago = pagoRepository
                .findTopByUsuarioIdOrderByFechaDesc(usuarioId)
                .orElseThrow(() -> new RuntimeException("No hay pagos"));

        return mapToResponse(pago);
    }

    // =====================================================
    // RESUMEN GENERAL (BOLETA)
    // =====================================================
    @Override
    public ResumenGeneralResponseDTO obtenerResumenPago(Long pagoId) {

        Pago pago = pagoRepository.findById(pagoId)
                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

        Contrato contrato = pago.getContrato();
        Usuario usuario = contrato.getUsuario();

        Persona persona = usuario.getPersona();
        Sede sede = usuario.getSede();

        BigDecimal totalBonificaciones = pago.getBonificaciones()
                .stream()
                .map(Bonificacion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDeducciones = pago.getDeducciones()
                .stream()
                .map(Deduccion::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAdelantos = pago.getAdelantos()
                .stream()
                .map(Adelanto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalDescuentos = totalDeducciones.add(totalAdelantos);

        BigDecimal neto = pago.getMontoActividad()
                .add(totalBonificaciones)
                .subtract(totalDescuentos);

        ResumenGeneralResponseDTO r = new ResumenGeneralResponseDTO();

        r.setCodigo(String.valueOf(usuario.getId())); // puedes cambiar luego
        r.setDni(persona.getDni());
        r.setSede(sede.getNombreSede());
        r.setCargo(obtenerCargo(usuario)); // mejorar después
        r.setTipoPago(contrato.getTipoPago());

        r.setPagoBase(contrato.getMontoBase().doubleValue());
        r.setMonto(pago.getMontoActividad().doubleValue());
        r.setTotalBonificaciones(totalBonificaciones.doubleValue());
        r.setTotalDescuentos(totalDescuentos.doubleValue());
        r.setNetoAPagar(neto.doubleValue());

        return r;
    }

    // =====================================================
    // MÉTODOS PRIVADOS
    // =====================================================
    private BigDecimal calcularMontoActividad(
            Contrato contrato,
            Integer horasTrabajadas) {

        if (contrato.getTipoPago() == Contrato.TipoPago.PAGO_MENSUAL) {
            return contrato.getMontoBase();
        }

        if (horasTrabajadas == null) {
            throw new RuntimeException("Debe enviar horasTrabajadas");
        }

        return contrato.getMontoBase()
                .multiply(BigDecimal.valueOf(horasTrabajadas));
    }

    private PagoResponseDTO mapToResponse(Pago pago) {

        PagoResponseDTO dto = new PagoResponseDTO();
        dto.setPagoId(pago.getId());
        dto.setFecha(pago.getFecha());
        dto.setMontoActividad(pago.getMontoActividad());
        dto.setNetoPagar(pago.getNetoPagar());

        return dto;
    }
private String obtenerCargo(Usuario usuario) {

    Optional<Administrativo> adminOpt =
            administrativoRepository.findByUsuarioId(usuario.getId());

    if (adminOpt.isPresent()) {
        return adminOpt.get()
                .getCargoAdministrativo()
                .getNombreCargo();
    }

    Optional<Docente> docenteOpt =
            docenteRepository.findByUsuarioId(usuario.getId());

    if (docenteOpt.isPresent()) {
        return docenteOpt.get()
                .getEspecialidadDocente()
                .getNombreEspecialidad();
    }

    return "SIN ASIGNACIÓN";
}
}