package com.example.actividades_app.service.Impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import com.example.actividades_app.enums.TipoAsistencia;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.dto.Pago.PlanillaAdministrativoDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.AsistenciaAdministrativoRepository;
import com.example.actividades_app.repository.ContratoRepository;
import com.example.actividades_app.service.PlanillaAdministrativoService;

@Service
@RequiredArgsConstructor
public class PlanillaAdministrativoServiceImpl implements PlanillaAdministrativoService {

        private final AsistenciaAdministrativoRepository asistenciaRepository;
        private final ContratoRepository contratoRepository;
        private final AdministrativoRepository administrativoRepository;

   @Override
public PlanillaAdministrativoDTO calcularPlanilla(Long idRecibido, LocalDate inicio, LocalDate fin) {
    
    // 1. DETERMINAR EL ADMINISTRATIVO REAL
    // Intentamos buscarlo primero como AdministrativoID, si no, como UsuarioID
    Administrativo admin = administrativoRepository.findById(idRecibido)
            .orElseGet(() -> administrativoRepository.findByUsuarioId(idRecibido)
            .orElseThrow(() -> new RuntimeException("No se encontró administrativo con ID o UsuarioID: " + idRecibido)));

    // Ahora tenemos los dos IDs garantizados
    Long realAdminId = admin.getId();
    Long realUsuarioId = admin.getUsuario().getId();

    // 2. CONTRATO: Buscamos siempre por el UsuarioID que ya sabemos que funciona
    Contrato contrato = contratoRepository.findByUsuarioId(realUsuarioId)
            .orElseThrow(() -> new RuntimeException("Contrato no encontrado para el usuario: " + realUsuarioId));

    BigDecimal sueldoBase = contrato.getMontoBase();

    // --- Cálculos de valor por tiempo ---
    int diasMes = contrato.getDiasLaborablesMes() != null ? contrato.getDiasLaborablesMes() : 30;
    int horasJornada = contrato.getHorasJornada() != null ? contrato.getHorasJornada() : 8;

    BigDecimal valorDia = sueldoBase.divide(BigDecimal.valueOf(diasMes), 2, RoundingMode.HALF_UP);
    BigDecimal valorHora = valorDia.divide(BigDecimal.valueOf(horasJornada), 2, RoundingMode.HALF_UP);
    BigDecimal valorMinuto = valorHora.divide(BigDecimal.valueOf(60), 4, RoundingMode.HALF_UP);

    // 3. ASISTENCIAS: Usamos el ID real de la tabla Administrativo
    List<AsistenciaAdministrativo> asistencias = asistenciaRepository
            .findByAdministrativo_IdAndFechaBetween(realAdminId, inicio, fin);

    // --- Procesamiento con Streams ---
    int faltas = (int) asistencias.stream()
            .filter(a -> a.getTipoAsistencia() == TipoAsistencia.FALTA)
            .count();
    BigDecimal descFaltas = valorDia.multiply(BigDecimal.valueOf(faltas));

    int permisos = (int) asistencias.stream()
            .filter(a -> a.getTipoAsistencia() == TipoAsistencia.PERMISO)
            .count();

    int minutosTardanza = asistencias.stream()
            .filter(a -> a.getTipoAsistencia() == TipoAsistencia.TARDANZA)
            .map(a -> a.getTardanza() == null ? 0 : a.getTardanza())
            .reduce(0, Integer::sum);
    BigDecimal descTardanza = valorMinuto.multiply(BigDecimal.valueOf(minutosTardanza));

    // --- Totales ---
    BigDecimal descuentoTotal = descFaltas.add(descTardanza);
    BigDecimal sueldoNeto = sueldoBase.subtract(descuentoTotal);

    return PlanillaAdministrativoDTO.builder()
            .administrativoId(realAdminId)
            .sueldoBase(sueldoBase)
            .faltas(faltas)
            .permisos(permisos)
            .minutosTardanza(minutosTardanza)
            .descuentoFaltas(descFaltas.setScale(2, RoundingMode.HALF_UP))
            .descuentoTardanza(descTardanza.setScale(2, RoundingMode.HALF_UP))
            .sueldoNeto(sueldoNeto.setScale(2, RoundingMode.HALF_UP))
            .build();
}
}