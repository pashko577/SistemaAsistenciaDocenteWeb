package com.example.actividades_app.service.Impl;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.enums.TipoAsistencia;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoResponseDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.AsistenciaAdministrativoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.AsistenciaAdministrativoService;

@Service
@RequiredArgsConstructor
@Transactional
public class AsistenciaAdministrativoServiceImpl implements AsistenciaAdministrativoService {

    private final AsistenciaAdministrativoRepository asistenciaRepository;
    private final AdministrativoRepository administrativoRepository;
    private final CronogramaAdministrativoRepository cronogramaRepository;

    private static final int TOLERANCIA_MINUTOS = 5;

    // =========================
    // REGISTRAR ASISTENCIA
    // =========================
    @Override
    public AsistenciaAdministrativoResponseDTO registrar(AsistenciaAdministrativoRequestDTO dto) {

        Administrativo admin = administrativoRepository.findById(dto.getAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

        CronogramaAdministrativo cronograma = cronogramaRepository
                .findById(dto.getCronogramaAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));

        // Verificar si ya existe asistencia
        if (asistenciaRepository.existsByAdministrativo_IdAndFecha(admin.getId(), dto.getFecha())) {
            throw new RuntimeException("Ya existe asistencia para ese día");
        }

        // Inicializamos tardanza
        int tardanza = 0;

        // Calcular tardanza solo si ingreso y cronograma no son nulos
        if (dto.getHoraIngreso() != null && cronograma.getHoraEntrada() != null) {
            long minutos = Duration.between(cronograma.getHoraEntrada(), dto.getHoraIngreso()).toMinutes();
            tardanza = (int) Math.max(minutos - TOLERANCIA_MINUTOS, 0);
        }

        // Determinar tipo de asistencia automáticamente
        TipoAsistencia tipo;
        if (dto.getHoraIngreso() == null && dto.getTipoAsistencia() != TipoAsistencia.PERMISO) {
            tipo = TipoAsistencia.FALTA;
        } else if (tardanza > 0) {
            tipo = TipoAsistencia.TARDANZA;
        } else {
            tipo = (dto.getTipoAsistencia() != null) ? dto.getTipoAsistencia() : TipoAsistencia.ASISTIO;
        }

        // Crear entidad
        AsistenciaAdministrativo asistencia = AsistenciaAdministrativo.builder()
                .horaIngreso(dto.getHoraIngreso())
                .horaSalida(dto.getHoraSalida())
                .salidaAlmuerzo(dto.getSalidaAlmuerzo())
                .retornoAlmuerzo(dto.getRetornoAlmuerzo())
                .fecha(dto.getFecha())
                .observaciones(dto.getObservaciones())
                .terno(dto.getTerno())
                .tardanza(tardanza)
                .tipoAsistencia(tipo)
                .administrativo(admin)
                .cronogramaAdministrativo(cronograma)
                .build();

        asistenciaRepository.save(asistencia);

        return mapToDTO(asistencia);
    }

    // =========================
    // ACTUALIZAR ASISTENCIA
    // =========================
    @Override
    public AsistenciaAdministrativoResponseDTO actualizar(Long id, AsistenciaAdministrativoRequestDTO dto) {

        AsistenciaAdministrativo asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        Administrativo admin = administrativoRepository.findById(dto.getAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

        CronogramaAdministrativo cronograma = cronogramaRepository.findById(dto.getCronogramaAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Cronograma no encontrado"));

        // Calcular tardanza
        int tardanza = 0;
        if (dto.getHoraIngreso() != null && cronograma.getHoraEntrada() != null) {
            long minutos = Duration.between(cronograma.getHoraEntrada(), dto.getHoraIngreso()).toMinutes();
            tardanza = (int) Math.max(minutos - TOLERANCIA_MINUTOS, 0);
        }

        // Determinar tipo de asistencia
        TipoAsistencia tipo;
        if (dto.getHoraIngreso() == null && dto.getTipoAsistencia() != TipoAsistencia.PERMISO) {
            tipo = TipoAsistencia.FALTA;
        } else if (tardanza > 0) {
            tipo = TipoAsistencia.TARDANZA;
        } else {
            tipo = (dto.getTipoAsistencia() != null) ? dto.getTipoAsistencia() : TipoAsistencia.ASISTIO;
        }

        // Actualizar datos
        asistencia.setHoraIngreso(dto.getHoraIngreso());
        asistencia.setHoraSalida(dto.getHoraSalida());
        asistencia.setSalidaAlmuerzo(dto.getSalidaAlmuerzo());
        asistencia.setRetornoAlmuerzo(dto.getRetornoAlmuerzo());
        asistencia.setFecha(dto.getFecha());
        asistencia.setObservaciones(dto.getObservaciones());
        asistencia.setTerno(dto.getTerno());
        asistencia.setTardanza(tardanza);
        asistencia.setTipoAsistencia(tipo);
        asistencia.setAdministrativo(admin);
        asistencia.setCronogramaAdministrativo(cronograma);

        asistenciaRepository.save(asistencia);

        return mapToDTO(asistencia);
    }

    // =========================
    // BUSCAR POR ID
    // =========================
    @Override
    public AsistenciaAdministrativoResponseDTO buscarPorId(Long id) {
        return asistenciaRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));
    }

    // =========================
    // LISTAR POR ADMINISTRATIVO
    // =========================
    @Override
    public List<AsistenciaAdministrativoResponseDTO> listarPorAdministrativo(Long administrativoId) {
        return asistenciaRepository.findByAdministrativo_Id(administrativoId)
                .stream().map(this::mapToDTO).toList();
    }

    // =========================
    // LISTAR POR PERIODO
    // =========================
    @Override
    public List<AsistenciaAdministrativoResponseDTO> listarPorPeriodo(Long administrativoId, LocalDate inicio,
            LocalDate fin) {
        return asistenciaRepository.findByAdministrativo_IdAndFechaBetween(administrativoId, inicio, fin)
                .stream().map(this::mapToDTO).toList();
    }

    // =========================
    // LISTAR POR FECHAS
    // =========================

    @Override
    public List<AsistenciaAdministrativoResponseDTO> listarPorFecha(LocalDate fecha) {
        return asistenciaRepository.findByFecha(fecha)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =========================
    // ELIMINAR
    // =========================
    @Override
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    // =========================
    // MAPPER
    // =========================
    private AsistenciaAdministrativoResponseDTO mapToDTO(AsistenciaAdministrativo entity) {
        return AsistenciaAdministrativoResponseDTO.builder()
                .id(entity.getId())
                .horaIngreso(entity.getHoraIngreso())
                .horaSalida(entity.getHoraSalida())
                .salidaAlmuerzo(entity.getSalidaAlmuerzo())
                .retornoAlmuerzo(entity.getRetornoAlmuerzo())
                .fecha(entity.getFecha())
                .observaciones(entity.getObservaciones())
                .tardanza(entity.getTardanza())
                .terno(entity.getTerno())
                .tipoAsistencia(entity.getTipoAsistencia())
                .administrativoId(entity.getAdministrativo().getId())
                .cronogramaAdministrativoId(entity.getCronogramaAdministrativo().getId())
                .build();
    }

}