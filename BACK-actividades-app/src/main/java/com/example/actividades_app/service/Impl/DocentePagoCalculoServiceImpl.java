package com.example.actividades_app.service.Impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Pago;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.Pago.PagoCalculoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoCalculoResponseDTO;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.DocentePagoCalculoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocentePagoCalculoServiceImpl implements DocentePagoCalculoService {

    private final UsuarioRepository usuarioRepository;

    private static final BigDecimal MINUTOS_POR_HORA = BigDecimal.valueOf(60);

    private static final BigDecimal FACTOR_WHATSAPP = BigDecimal.valueOf(0.1);
    private static final BigDecimal FACTOR_FICHA = BigDecimal.valueOf(0.3);
    private static final BigDecimal FACTOR_AGENDA = BigDecimal.valueOf(0.5);

    // =====================================================
    // CALCULAR VALOR POR MINUTO
    // =====================================================
    private BigDecimal calcularValorPorMinuto(BigDecimal tarifaHora) {

        if (tarifaHora == null)
            return BigDecimal.ZERO;

        return tarifaHora.divide(
                MINUTOS_POR_HORA,
                6,
                RoundingMode.HALF_UP
        );
    }

    // =====================================================
    // CALCULAR PAGO POR HORAS
    // =====================================================
    @Override
    public BigDecimal calcularPagoPorHoras(
            BigDecimal horasTrabajadas,
            BigDecimal tarifaHora
    ) {

        if (horasTrabajadas == null || tarifaHora == null)
            return BigDecimal.ZERO;

        return horasTrabajadas.multiply(tarifaHora)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // DESCUENTO POR TARDANZA
    // =====================================================
    @Override
    public BigDecimal calcularDescuentoTardanza(
            Integer minutosTardanza,
            BigDecimal tarifaHora
    ) {

        if (minutosTardanza == null || minutosTardanza <= 0)
            return BigDecimal.ZERO;

        BigDecimal valorMinuto = calcularValorPorMinuto(tarifaHora);

        return valorMinuto.multiply(
                BigDecimal.valueOf(minutosTardanza)
        ).setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // DESCUENTO POR FALTAS
    // =====================================================
    @Override
    public BigDecimal calcularDescuentoFaltas(
            Integer minutosFalta,
            BigDecimal tarifaHora,
            BigDecimal factorDescuento
    ) {

        if (minutosFalta == null || minutosFalta <= 0)
            return BigDecimal.ZERO;

        BigDecimal valorMinuto = calcularValorPorMinuto(tarifaHora);

        return valorMinuto
                .multiply(BigDecimal.valueOf(minutosFalta))
                .multiply(factorDescuento)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // CALCULAR CUMPLIMIENTO
    // =====================================================
    @Override
    public BigDecimal calcularCumplimiento(
            Integer whatsapp,
            Integer ficha,
            Integer agenda
    ) {

        BigDecimal total =
                FACTOR_WHATSAPP.multiply(BigDecimal.valueOf(whatsapp))
                        .add(FACTOR_FICHA.multiply(BigDecimal.valueOf(ficha)))
                        .add(FACTOR_AGENDA.multiply(BigDecimal.valueOf(agenda)));

        if (total.compareTo(BigDecimal.valueOf(11)) >= 0) {

            return FACTOR_WHATSAPP.multiply(BigDecimal.valueOf(whatsapp))
                    .add(FACTOR_AGENDA.multiply(BigDecimal.valueOf(agenda)))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    // =====================================================
    // CALCULAR PAGO NETO
    // =====================================================
    @Override
    public BigDecimal calcularPagoNeto(
            BigDecimal montoBase,
            BigDecimal descuentoTardanza,
            BigDecimal descuentoFaltas,
            BigDecimal bonificaciones
    ) {

        if (bonificaciones == null)
            bonificaciones = BigDecimal.ZERO;

        return montoBase
                .subtract(descuentoTardanza)
                .subtract(descuentoFaltas)
                .add(bonificaciones)
                .setScale(2, RoundingMode.HALF_UP);
    }

    // =====================================================
    // CALCULO COMPLETO
    // =====================================================
    @Override
    public PagoCalculoResponseDTO calcularPagoCompleto(
            PagoCalculoRequestDTO request
    ) {

        BigDecimal montoBase;

        if (request.getTipoPago() == Pago.TipoPago.POR_HORA) {

            montoBase = calcularPagoPorHoras(
                    request.getHorasTrabajadas(),
                    request.getTarifaHora()
            );

        } else {

            montoBase = request.getSueldoMensual();
        }

        BigDecimal descuentoTardanza =
                calcularDescuentoTardanza(
                        request.getMinutosTardanza(),
                        request.getTarifaHora()
                );

        BigDecimal descuentoFaltas =
                calcularDescuentoFaltas(
                        request.getFaltas(),
                        request.getTarifaHora(),
                        request.getFactorDescuentoFalta()
                );

        BigDecimal descuentoCumplimiento =
                calcularCumplimiento(
                        request.getIncumplimientosWhatsapp(),
                        request.getIncumplimientosFicha(),
                        request.getIncumplimientosAgenda()
                );

        BigDecimal neto =
                calcularPagoNeto(
                        montoBase,
                        descuentoTardanza.add(descuentoCumplimiento),
                        descuentoFaltas,
                        request.getBonificaciones()
                );

        return PagoCalculoResponseDTO.builder()
                .montoBase(montoBase)
                .descuentoTardanza(descuentoTardanza)
                .descuentoFaltas(descuentoFaltas)
                .cumplimientoDescuento(descuentoCumplimiento)
                .bonificaciones(request.getBonificaciones())
                .netoPagar(neto)
                .build();
    }

    // =====================================================
    // GENERAR ENTIDAD PAGO
    // =====================================================
    @Override
    public Pago generarPagoDesdeCalculo(
            PagoCalculoRequestDTO request
    ) {

        Usuario usuario = usuarioRepository.findById(
                request.getUsuarioId()
        ).orElseThrow(() ->
                new RuntimeException("Usuario no encontrado")
        );

        PagoCalculoResponseDTO calculo =
                calcularPagoCompleto(request);

        return Pago.builder()
                .usuario(usuario)
                .tipoPago(request.getTipoPago())
                .fecha(LocalDate.now())
                .montoActividad(calculo.getMontoBase())
                .netoPagar(calculo.getNetoPagar())
                .build();
    }

}
