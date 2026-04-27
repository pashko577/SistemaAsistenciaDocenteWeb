package com.example.actividades_app.service.Impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;
import com.example.actividades_app.model.Entity.AsistenciaDocente;
import com.example.actividades_app.model.Entity.CronogramaDiario;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDiariaDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloRegistroAsistencia.AsistenciaDocenteResponseDTO;
import com.example.actividades_app.repository.AsistenciaDocenteRepository;
import com.example.actividades_app.repository.CronogramaDiarioRepository;
import com.example.actividades_app.service.AsistenciaDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AsistenciaDocenteServiceImpl implements AsistenciaDocenteService {

    private final AsistenciaDocenteRepository asistenciaRepository;
    private final CronogramaDiarioRepository cronogramaDiarioRepository;

    private static final int TOLERANCIA_MINUTOS = 5;

    @Override
    public AsistenciaDocenteResponseDTO registrar(AsistenciaDocenteRequestDTO dto) {
        // 1. Validaciones previas
        if (asistenciaRepository.existsByCronogramaDiarioId(dto.getCronogramaDiarioId())) {
            throw new RuntimeException("La asistencia ya fue registrada");
        }

        CronogramaDiario cronograma = cronogramaDiarioRepository.findById(dto.getCronogramaDiarioId())
                .orElseThrow(() -> new RuntimeException("Cronograma diario no encontrado"));

        LocalTime horaInicio = cronograma.getCronogramaDocente().getHorarioBloque().getHoraInicio();

        // 2. Cálculo de minutos de tardanza
        int minutosTardanza = 0;
        if (dto.getHoraEntradaDoc() != null) {
            long diferencia = java.time.Duration.between(horaInicio, dto.getHoraEntradaDoc()).toMinutes();
            // Aplicamos la constante de tolerancia que ya tenías definida
            minutosTardanza = (int) Math.max(diferencia - TOLERANCIA_MINUTOS, 0);
        }

        // 3. Determinación del Estado
        EstadoAsistenciaDocente estado;
        if (dto.getEstadoAsistencia() == EstadoAsistenciaDocente.PERMISO) {
            estado = EstadoAsistenciaDocente.PERMISO;
            minutosTardanza = 0; // Si tiene permiso, no se le cuenta tardanza
        } else if (dto.getHoraEntradaDoc() == null) {
            estado = EstadoAsistenciaDocente.FALTA;
            minutosTardanza = 0; // En falta no hay "minutos", hay inasistencia
        } else if (minutosTardanza > 0) {
            estado = EstadoAsistenciaDocente.TARDANZA;
        } else {
            estado = EstadoAsistenciaDocente.PUNTUAL;
        }

        // 4. Construcción y Persistencia
        AsistenciaDocente asistencia = AsistenciaDocente.builder()
                .horaEntradaDoc(dto.getHoraEntradaDoc())
                .horaSalidaDoc(dto.getHoraSalidaDoc())
                .observacion(dto.getObservacion())
                .materialClase(dto.getMaterialClase())
                .usoTerno(dto.getUsoTerno())
                .minutosTardanza(minutosTardanza) // <--- ASIGNAMOS EL VALOR CALCULADO
                .estadoAsistencia(estado)
                .cronogramaDiario(cronograma)
                .build();

        asistenciaRepository.save(asistencia);

        return mapToResponse(asistencia);
    }

    @Override
    public List<AsistenciaDocenteResponseDTO> listar() {

        return asistenciaRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<AsistenciaDiariaDTO> listarControlDiario(LocalDate fecha) {
        return cronogramaDiarioRepository.obtenerControlDiarioPorFecha(fecha);
    }

    // No olvides actualizar el mapeador
    private AsistenciaDocenteResponseDTO mapToResponse(AsistenciaDocente a) {
        return AsistenciaDocenteResponseDTO.builder()
                .id(a.getId())
                .horaEntradaDoc(a.getHoraEntradaDoc())
                .horaSalidaDoc(a.getHoraSalidaDoc())
                .minutosTardanza(a.getMinutosTardanza()) // <--- LO ENVIAMOS AL DTO
                .observacion(a.getObservacion())
                .materialClase(a.getMaterialClase())
                .usoTerno(a.getUsoTerno())
                .estadoAsistencia(a.getEstadoAsistencia())
                .cronogramaDiarioId(a.getCronogramaDiario().getId())
                .build();
    }
}