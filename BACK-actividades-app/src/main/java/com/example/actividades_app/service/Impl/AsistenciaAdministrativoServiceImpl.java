package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.AsistenciaAdministrativo;
import com.example.actividades_app.model.Entity.CronogramaAdministrativo;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AsistenciaAdministrativoResponseDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.AsistenciaAdministrativoRepository;
import com.example.actividades_app.repository.CronogramaAdministrativoRepository;
import com.example.actividades_app.service.AsistenciaAdministrativoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaAdministrativoServiceImpl implements AsistenciaAdministrativoService {

    private final AsistenciaAdministrativoRepository asistenciaRepository;
    private final AdministrativoRepository administrativoRepository;
    private final CronogramaAdministrativoRepository cronogramaRepository;

    // =====================================================
    // REGISTRAR ASISTENCIA
    // =====================================================
    @Override
    public AsistenciaAdministrativoResponseDTO registrar(AsistenciaAdministrativoRequestDTO dto) {

        Administrativo administrativo = administrativoRepository.findById(dto.getAdministrativoId())
                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

        CronogramaAdministrativo cronograma = cronogramaRepository
                .findByAdministrativoIdAndFecha(dto.getAdministrativoId(), dto.getFecha())
                .orElseThrow(() -> new RuntimeException("No existe cronograma para esa fecha"));

        // Validar duplicado
        asistenciaRepository
                .findByAdministrativoIdAndFecha(dto.getAdministrativoId(), dto.getFecha())
                .ifPresent(a -> {
                    throw new RuntimeException("La asistencia ya fue registrada");
                });

        AsistenciaAdministrativo asistencia = AsistenciaAdministrativo.builder()
                .horaIngreso(dto.getHoraIngreso())
                .horaSalida(dto.getHoraSalida())
                .salidaAlmuerzo(dto.getSalidaAlmuerzo())
                .retornoAlmuerzo(dto.getRetornoAlmuerzo())
                .fecha(dto.getFecha())
                .observaciones(dto.getObservaciones())
                .terno(dto.getTerno())
                .administrativo(administrativo)
                .cronogramaAdministrativo(cronograma)
                .tardanza(calcularTardanza(dto.getHoraIngreso(), cronograma.getHoraEntrada()))
                .build();

        asistenciaRepository.save(asistencia);

        return mapToDTO(asistencia);
    }

    // =====================================================
    // ACTUALIZAR
    // =====================================================
    @Override
    public AsistenciaAdministrativoResponseDTO actualizar(Long id,
            AsistenciaAdministrativoRequestDTO dto) {

        AsistenciaAdministrativo asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        asistencia.setHoraIngreso(dto.getHoraIngreso());
        asistencia.setHoraSalida(dto.getHoraSalida());
        asistencia.setSalidaAlmuerzo(dto.getSalidaAlmuerzo());
        asistencia.setRetornoAlmuerzo(dto.getRetornoAlmuerzo());
        asistencia.setObservaciones(dto.getObservaciones());
        asistencia.setTerno(dto.getTerno());

        asistencia.setTardanza(
                calcularTardanza(
                        dto.getHoraIngreso(),
                        asistencia.getCronogramaAdministrativo().getHoraEntrada()));

        asistenciaRepository.save(asistencia);

        return mapToDTO(asistencia);
    }

    // =====================================================
    // ELIMINAR
    // =====================================================
    @Override
    public void eliminar(Long id) {
        asistenciaRepository.deleteById(id);
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================
    @Override
    public AsistenciaAdministrativoResponseDTO buscarPorId(Long id) {

        AsistenciaAdministrativo asistencia = asistenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asistencia no encontrada"));

        return mapToDTO(asistencia);
    }

    // =====================================================
    // LISTAR POR ADMINISTRATIVO
    // =====================================================
    @Override
    public List<AsistenciaAdministrativoResponseDTO> listarPorAdministrativo(Long administrativoId) {

        return asistenciaRepository.findByAdministrativoId(administrativoId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =====================================================
    // BUSCAR POR ADMINISTRATIVO + FECHA
    // =====================================================
    @Override
    public AsistenciaAdministrativoResponseDTO buscarPorAdministrativoYFecha(
            Long administrativoId,
            LocalDate fecha) {

        AsistenciaAdministrativo asistencia = asistenciaRepository
                .findByAdministrativoIdAndFecha(administrativoId, fecha)
                .orElseThrow(() -> new RuntimeException("No existe asistencia"));

        return mapToDTO(asistencia);
    }

    // =====================================================
    // CALCULAR TARDANZA
    // =====================================================
    private Integer calcularTardanza(LocalTime ingreso, LocalTime horaEntrada) {

        if (ingreso == null || horaEntrada == null)
            return 0;

        if (!ingreso.isAfter(horaEntrada))
            return 0;

        return (int) java.time.Duration
                .between(horaEntrada, ingreso)
                .toMinutes();
    }

    // =====================================================
    // MAPPER ENTITY -> DTO
    // =====================================================
    private AsistenciaAdministrativoResponseDTO mapToDTO(AsistenciaAdministrativo a) {

        AsistenciaAdministrativoResponseDTO dto = new AsistenciaAdministrativoResponseDTO();

        dto.setId(a.getId());
        dto.setHoraIngreso(a.getHoraIngreso());
        dto.setHoraSalida(a.getHoraSalida());
        dto.setSalidaAlmuerzo(a.getSalidaAlmuerzo());
        dto.setRetornoAlmuerzo(a.getRetornoAlmuerzo());
        dto.setFecha(a.getFecha());
        dto.setObservaciones(a.getObservaciones());
        dto.setTardanza(a.getTardanza());
        dto.setTerno(a.getTerno());
        dto.setAdministrativoId(a.getAdministrativo().getId());
        dto.setCronogramaAdministrativoId(a.getCronogramaAdministrativo().getId());

        return dto;
    }
}