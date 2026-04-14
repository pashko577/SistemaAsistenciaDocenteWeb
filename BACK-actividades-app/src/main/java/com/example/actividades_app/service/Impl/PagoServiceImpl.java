package com.example.actividades_app.service.Impl;

import com.example.actividades_app.enums.EstadoAdelanto;
import com.example.actividades_app.enums.TipoAsistencia;
import com.example.actividades_app.model.Entity.*;
import com.example.actividades_app.model.dto.ModuloDocente.PlanillaDocenteDTO;
import com.example.actividades_app.model.dto.Pago.PagoRequestDTO;
import com.example.actividades_app.model.dto.Pago.PagoResponseDTO;
import com.example.actividades_app.model.dto.Pago.PlanillaAdministrativoDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.BonificacionRequestDTO;
import com.example.actividades_app.model.dto.Reporte.BonificacionResponseDTO;
import com.example.actividades_app.model.dto.Reporte.DescuentoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.DescuentoResponseDTO;
import com.example.actividades_app.model.dto.Reporte.ResumenGeneralResponseDTO;
import com.example.actividades_app.repository.*;
import com.example.actividades_app.service.AdelantoService;
import com.example.actividades_app.service.PagoService;
import com.example.actividades_app.service.PlanillaAdministrativoService;
import com.example.actividades_app.service.PlanillaDocenteService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PagoServiceImpl implements PagoService {

        private final PagoRepository pagoRepository;
        private final ContratoRepository contratoRepository;

        private final TipoDeduccionRepository tipoDeduccionRepository;
        private final AdministrativoRepository administrativoRepository;

        private final DocenteRepository docenteRepository;
        private final AsistenciaAdministrativoRepository asistenciaAdministrativoRepository;
        private final PlanillaAdministrativoService planillaAdministrativoService;
        private final PlanillaDocenteService planillaDocenteService;

        private final AdelantoRepository adelantoRepository;
        private final AdelantoService adelantoService;

        // =====================================================
        // CREAR PAGO
        // =====================================================
        @Override
        public PagoResponseDTO crear(PagoRequestDTO dto) {
                int mesPlanilla = dto.getFecha().getMonthValue();
                int anioPlanilla = dto.getFecha().getYear();
                // -----------------------------
                // OBTENER CONTRATO
                // -----------------------------
                Contrato contrato = contratoRepository.findById(dto.getContratoId())
                                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

                // Validar pago mensual único
                if (contrato.getTipoPago() == Contrato.TipoPago.PAGO_MENSUAL) {
                        validarPagoMensualUnico(contrato.getId(), dto.getFecha());
                }

                Usuario usuario = contrato.getUsuario();

                // -----------------------------
                // CREAR OBJETO PAGO
                // -----------------------------
                Pago pago = Pago.builder()
                                .fecha(dto.getFecha())
                                .contrato(contrato)
                                .montoActividad(BigDecimal.ZERO)
                                .netoPagar(BigDecimal.ZERO)
                                .build();

                // 🔥 PASO VITAL: Guardar el pago para generar el ID (pagoid)
                // Esto evita el TransientObjectException
                pago = pagoRepository.saveAndFlush(pago);
                // -----------------------------
                // LISTAS RELACIONADAS
                // -----------------------------
                List<Adelanto> listaAdelantos = new ArrayList<>();
                List<Bonificacion> listaBonificaciones = new ArrayList<>();
                List<Deduccion> listaDeducciones = new ArrayList<>();

                BigDecimal totalAdelantos = BigDecimal.ZERO;
                BigDecimal totalBonificaciones = BigDecimal.ZERO;
                BigDecimal totalDeducciones = BigDecimal.ZERO;

                // -----------------------------
                // CALCULAR MONTO ACTIVIDAD (HORAS O MENSUAL)
                // -----------------------------
                BigDecimal montoActividad = calcularMontoActividad(contrato, usuario, dto.getFecha());
                pago.setMontoActividad(montoActividad);

                // -----------------------------
                // ADELANTOS
                // -----------------------------
                // 1. Buscamos los adelantos que están PENDIENTES en la BD para este usuario
              if (dto.getAdelantoIds() != null && !dto.getAdelantoIds().isEmpty()) {
        List<Adelanto> adelantosSeleccionados = adelantoRepository.findAllById(dto.getAdelantoIds());

        for (Adelanto adelanto : adelantosSeleccionados) {
            // FIX 2: Vincular bidireccionalmente
            adelanto.setPago(pago); 
            adelanto.setEstado(EstadoAdelanto.APLICADO);
            
            // Acumular para el cálculo del neto
            totalAdelantos = totalAdelantos.add(adelanto.getMonto());
            listaAdelantos.add(adelanto); // Agregar a la lista que se asociará al pago
        }
        // Guardar cambios en los adelantos (setear pagoid y estado)
        adelantoRepository.saveAll(adelantosSeleccionados);
    }
                // -----------------------------
                // BONIFICACIONES
                // -----------------------------
                if (dto.getBonificaciones() != null) {
                        for (BonificacionRequestDTO b : dto.getBonificaciones()) {
                                if (b.getMonto().compareTo(BigDecimal.ZERO) <= 0)
                                        continue;

                                Bonificacion bonificacion = Bonificacion.builder()
                                                .nombre(b.getNombre())
                                                .monto(b.getMonto())
                                                .pago(pago)
                                                .build();
                                listaBonificaciones.add(bonificacion);
                                totalBonificaciones = totalBonificaciones.add(b.getMonto());
                        }
                }

                // -----------------------------
                // DEDUCCIONES AUTOMÁTICAS
                // -----------------------------
                BigDecimal descuentoAutomatica = BigDecimal.ZERO;

                // Docente
                Optional<Docente> docenteOpt = docenteRepository.findByUsuarioId(usuario.getId());
                if (docenteOpt.isPresent()) {
                        Docente docente = docenteOpt.get();

                        LocalDate inicioMes = dto.getFecha().withDayOfMonth(1);
                        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

                        PlanillaDocenteDTO planilla = planillaDocenteService.calcularPlanilla(
                                        docente.getId(),
                                        inicioMes,
                                        finMes);

                        descuentoAutomatica = planilla.getDescuentoFaltas()
                                        .add(planilla.getDescuentoTardanza())
                                        .add(planilla.getDescuentoCriterios());
                }

                // Administrativo
                Optional<Administrativo> adminOpt = administrativoRepository.findByUsuarioId(usuario.getId());
                if (adminOpt.isPresent()) {
                        Administrativo admin = adminOpt.get();

                        LocalDate inicioMes = dto.getFecha().withDayOfMonth(1);
                        LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

                        PlanillaAdministrativoDTO planilla = planillaAdministrativoService.calcularPlanilla(
                                        admin.getId(),
                                        inicioMes,
                                        finMes);

                        descuentoAutomatica = descuentoAutomatica.add(
                                        planilla.getDescuentoFaltas().add(planilla.getDescuentoTardanza()));
                }

                // Crear deducción automática si aplica
                if (descuentoAutomatica.compareTo(BigDecimal.ZERO) > 0) {
                        TipoDeduccion tipoAutomatica = tipoDeduccionRepository
                                        .findByNombre("Automática")
                                        .orElseThrow(() -> new RuntimeException(
                                                        "Debe existir tipo deducción 'Automática'"));

                        Deduccion deduccion = Deduccion.builder()
                                        .tipoDeduccion(tipoAutomatica)
                                        .observaciones("Descuento automático (faltas, tardanzas y criterios)")
                                        .monto(descuentoAutomatica)
                                        .pago(pago)
                                        .build();

                        listaDeducciones.add(deduccion);
                        totalDeducciones = totalDeducciones.add(descuentoAutomatica);
                }

                // -----------------------------
                // DEDUCCIONES MANUALES
                // -----------------------------
                if (dto.getDeducciones() != null) {
                        for (DescuentoRequestDTO d : dto.getDeducciones()) {
                                if (d.getTipoDeduccionId() == null || d.getMonto().compareTo(BigDecimal.ZERO) <= 0)
                                        continue;

                                TipoDeduccion tipo = tipoDeduccionRepository.findById(d.getTipoDeduccionId())
                                                .orElseThrow(() -> new RuntimeException(
                                                                "Tipo deducción no encontrado"));

                                Deduccion deduccion = Deduccion.builder()
                                                .tipoDeduccion(tipo)
                                                .observaciones(d.getObservaciones())
                                                .monto(d.getMonto())
                                                .pago(pago)
                                                .build();

                                listaDeducciones.add(deduccion);

                                // REGLA DE NEGOCIO: "Seguro" o "ESSALUD" no se descuentan del Total Neto del empleado
                                String nombreTipo = tipo.getNombre().toLowerCase();
                                if (!nombreTipo.contains("seguro") && !nombreTipo.contains("essalud")) {
                                        totalDeducciones = totalDeducciones.add(d.getMonto());
                                }
                        }
                }

                // -----------------------------
                // ASOCIAR LISTAS
                // -----------------------------
                pago.setAdelantos(listaAdelantos);
                pago.setBonificaciones(listaBonificaciones);
                pago.setDeducciones(listaDeducciones);

                // -----------------------------
                // CALCULAR NETO
                // -----------------------------
                recalcularNeto(pago, totalBonificaciones, totalDeducciones, totalAdelantos);

                // -----------------------------
                // GUARDAR PAGO
                // -----------------------------
                pagoRepository.save(pago);

                return mapToResponse(pago);
        }

        // =====================================================
        // ACTUALIZAR PAGO
        // =====================================================
        @Override
        public PagoResponseDTO actualizar(Long id, PagoRequestDTO dto) {
                Pago pago = pagoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

                pago.setFecha(dto.getFecha());

                BigDecimal montoActividad = calcularMontoActividad(pago.getContrato(), pago.getContrato().getUsuario(),
                                dto.getFecha());
                pago.setMontoActividad(montoActividad);

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
        // BUSCAR
        // =====================================================
        @Override
        public PagoResponseDTO buscarPorId(Long id) {
                return mapToResponse(
                                pagoRepository.findById(id)
                                                .orElseThrow(() -> new RuntimeException("Pago no encontrado")));
        }

        // =====================================================
        // LISTAR PAGOS
        // =====================================================
        @Override
        public List<PagoResponseDTO> listarPorUsuario(Long usuarioId) {
                return pagoRepository.findByContratoUsuarioId(usuarioId)
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
        public List<PagoResponseDTO> listarPorRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
                return pagoRepository.findByFechaBetween(fechaInicio, fechaFin)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Override
        public List<PagoResponseDTO> listarPorUsuarioYRangoFechas(Long usuarioId, LocalDate fechaInicio,
                        LocalDate fechaFin) {
                return pagoRepository.findByContratoUsuarioIdAndFechaBetween(usuarioId, fechaInicio, fechaFin)
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Override
        public PagoResponseDTO obtenerUltimoPago(Long usuarioId) {
                Pago pago = pagoRepository.findTopByContratoUsuarioIdOrderByFechaDesc(usuarioId)
                                .orElseThrow(() -> new RuntimeException("No existen pagos para el usuario"));
                return mapToResponse(pago);
        }

        // =====================================================
        // RESUMEN GENERAL
        // =====================================================
        @Override
        public ResumenGeneralResponseDTO obtenerResumenPago(Long pagoId) {

                Pago pago = pagoRepository.findById(pagoId)
                                .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

                Contrato contrato = pago.getContrato();
                Usuario usuario = contrato.getUsuario();
                Persona persona = usuario.getPersona();

                BigDecimal totalBonificaciones = Optional.ofNullable(pago.getBonificaciones())
                                .orElse(List.of())
                                .stream()
                                .map(Bonificacion::getMonto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalDeducciones = Optional.ofNullable(pago.getDeducciones())
                                .orElse(List.of())
                                .stream()
                                .map(Deduccion::getMonto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal totalAdelantos = Optional.ofNullable(pago.getAdelantos())
                                .orElse(List.of())
                                .stream()
                                .map(Adelanto::getMonto)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal neto = pago.getMontoActividad()
                                .add(totalBonificaciones)
                                .subtract(totalDeducciones.add(totalAdelantos));

                ResumenGeneralResponseDTO r = new ResumenGeneralResponseDTO();
                r.setCodigo(String.valueOf(usuario.getId()));
                r.setDni(persona.getDni());
                r.setSede(usuario.getSede().getNombreSede());
                r.setCargo(obtenerCargo(usuario));
                r.setTipoPago(contrato.getTipoPago());
                r.setPagoBase(contrato.getMontoBase().doubleValue());
                r.setMonto(pago.getMontoActividad().doubleValue());
                r.setTotalBonificaciones(totalBonificaciones.doubleValue());
                r.setTotalDescuentos(totalDeducciones.add(totalAdelantos).doubleValue());
                r.setNetoAPagar(neto.doubleValue());

                return r;
        }

        // =====================================================
        // MÉTODOS PRIVADOS
        // =====================================================
        private BigDecimal calcularMontoActividad(Contrato contrato, Usuario usuario, LocalDate fecha) {
                if (contrato.getTipoPago() == Contrato.TipoPago.PAGO_MENSUAL) {
                        return contrato.getMontoBase();
                }

                BigDecimal horasTrabajadas = calcularHorasTrabajadas(usuario, fecha);
                return contrato.getMontoBase().multiply(horasTrabajadas);
        }

        private void recalcularNeto(Pago pago, BigDecimal bonificaciones, BigDecimal deducciones,
                        BigDecimal adelantos) {
                BigDecimal neto = pago.getMontoActividad()
                                .add(bonificaciones)
                                .subtract(deducciones.add(adelantos));
                pago.setNetoPagar(neto);
        }

        private void validarPagoMensualUnico(Long contratoId, LocalDate fecha) {
                LocalDate inicioMes = fecha.withDayOfMonth(1);
                LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

                boolean existe = pagoRepository.existsByContratoIdAndFechaBetween(contratoId, inicioMes, finMes);
                if (existe)
                        throw new RuntimeException("Ya existe un pago registrado para este mes");
        }

        private BigDecimal calcularHorasTrabajadas(Usuario usuario, LocalDate fecha) {
                Optional<Administrativo> adminOpt = administrativoRepository.findByUsuarioId(usuario.getId());
                return adminOpt.map(admin -> calcularHorasAdministrativo(admin, fecha)).orElse(BigDecimal.ZERO);
        }

        private BigDecimal calcularHorasAdministrativo(Administrativo admin, LocalDate fecha) {
                LocalDate inicioMes = fecha.withDayOfMonth(1);
                LocalDate finMes = inicioMes.withDayOfMonth(inicioMes.lengthOfMonth());

                List<AsistenciaAdministrativo> asistencias = asistenciaAdministrativoRepository
                                .findByAdministrativo_IdAndFechaBetween(admin.getId(), inicioMes, finMes);

                long minutosTotales = asistencias.stream()
                                .filter(a -> a.getHoraIngreso() != null && a.getHoraSalida() != null)
                                .mapToLong(a -> Duration.between(a.getHoraIngreso(), a.getHoraSalida()).toMinutes())
                                .sum();

                return BigDecimal.valueOf(minutosTotales)
                                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        }

        private PagoResponseDTO mapToResponse(Pago pago) {
                PagoResponseDTO dto = new PagoResponseDTO();
                Contrato contrato = pago.getContrato();
                Usuario usuario = contrato.getUsuario();
                Persona persona = usuario.getPersona();

                // 1. Datos básicos del Pago
                dto.setPagoId(pago.getId());
                dto.setFecha(pago.getFecha());

                if (pago.getContrato() != null) {
                        dto.setContratoId(pago.getContrato().getId());
                        dto.setTipoPago(pago.getContrato().getTipoPago().name());
                        dto.setMontoBase(pago.getContrato().getMontoBase());

                        dto.setNombreCompleto(persona.getNombres() + " " + persona.getApellidos());
                        dto.setDni(persona.getDni());
                        dto.setSede(usuario.getSede() != null ? usuario.getSede().getNombreSede() : "N/A");
                        dto.setCargo(obtenerCargo(usuario)); // Reutilizamos tu método existente
                }

                dto.setMontoActividad(pago.getMontoActividad());
                dto.setNetoPagar(pago.getNetoPagar());

                // 2. MAPEAR TOTALES (Esto es lo que salía null en tu JSON)
                BigDecimal totalBono = pago.getBonificaciones() != null ? pago.getBonificaciones().stream()
                                .map(Bonificacion::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

                BigDecimal totalDeduc = pago.getDeducciones() != null ? pago.getDeducciones().stream()
                                .map(Deduccion::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

                BigDecimal totalAdel = pago.getAdelantos() != null ? pago.getAdelantos().stream()
                                .map(Adelanto::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add) : BigDecimal.ZERO;

                dto.setTotalBonificaciones(totalBono);
                dto.setTotalDeducciones(totalDeduc);
                dto.setTotalAdelantos(totalAdel);

                // 3. MAPEAR LISTA DE ADELANTOS (Con todos sus campos)
                dto.setAdelantos(pago.getAdelantos() == null ? List.of() : pago.getAdelantos().stream().map(a -> {
                        AdelantoResponseDTO dtoA = new AdelantoResponseDTO();
                        dtoA.setId(a.getId());
                        dtoA.setNombre(a.getNombre());
                        dtoA.setMonto(a.getMonto());
                        dtoA.setEstado(a.getEstado() != null ? a.getEstado() : null);
                        dtoA.setUsuarioId(a.getUsuario() != null ? a.getUsuario().getId() : null);
                        dtoA.setPagoId(pago.getId());

                        if (a.getUsuario() != null && a.getUsuario().getPersona() != null) {
                                Persona p = a.getUsuario().getPersona();
                                dtoA.setNombreCompletoPersonal(p.getNombres() + " " + p.getApellidos());
                                dtoA.setDniPersonal(p.getDni());
                        }
                        return dtoA;
                }).toList());

                // 4. MAPEAR LISTA DE BONIFICACIONES
                dto.setBonificaciones(pago.getBonificaciones() == null ? List.of()
                                : pago.getBonificaciones().stream().map(b -> {
                                        BonificacionResponseDTO dtoB = new BonificacionResponseDTO();
                                        dtoB.setNombre(b.getNombre());
                                        dtoB.setMonto(b.getMonto());
                                        return dtoB;
                                }).toList());

                // 5. MAPEAR LISTA DE DEDUCCIONES
                dto.setDeducciones(pago.getDeducciones() == null ? List.of() : pago.getDeducciones().stream().map(d -> {
                        DescuentoResponseDTO dtoD = new DescuentoResponseDTO();
                        dtoD.setMonto(d.getMonto());
                        dtoD.setObservaciones(d.getObservaciones());
                        if (d.getTipoDeduccion() != null) {
                                dtoD.setTipoDeduccionId(d.getTipoDeduccion().getId());
                        }
                        return dtoD;
                }).toList());

                return dto;
        }

        // =====================================================
        // OBTENER CARGO DEL USUARIO (ADMINISTRATIVO O DOCENTE)
        // =====================================================
        private String obtenerCargo(Usuario usuario) {
                return administrativoRepository.findByUsuarioId(usuario.getId())
                                .map(a -> a.getCargoAdministrativo().getNombreCargo())
                                .orElseGet(() -> docenteRepository.findByUsuarioId(usuario.getId())
                                                .map(d -> d.getEspecialidadDocente().getNombreEspecialidad())
                                                .orElse("SIN ASIGNACIÓN"));
        }

        /*
         * private int calcularDiasProgramados(
         * Long administrativoId,
         * LocalDate inicio,
         * LocalDate fin) {
         * 
         * List<CronogramaAdministrativo> cronogramas =
         * cronogramaAdministrativoRepository
         * .findByAdministrativoId(administrativoId);
         * 
         * int dias = 0;
         * 
         * for (LocalDate fecha = inicio; !fecha.isAfter(fin); fecha =
         * fecha.plusDays(1)) {
         * 
         * String dia = fecha.getDayOfWeek().name();
         * 
         * for (CronogramaAdministrativo c : cronogramas) {
         * if (c.getDiaSemana().name().equals(dia)) {
         * dias++;
         * break;
         * }
         * }
         * }
         * 
         * return dias;
         * }
         */
}