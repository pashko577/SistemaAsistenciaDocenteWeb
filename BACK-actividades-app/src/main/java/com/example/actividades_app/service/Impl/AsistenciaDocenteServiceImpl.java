package com.example.actividades_app.service.Impl;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.EstadoAsistenciaDocente;
import com.example.actividades_app.model.Entity.AsistenciaDocente;
import com.example.actividades_app.model.Entity.CronogramaDiario;
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

    if (asistenciaRepository.existsByCronogramaDiarioId(dto.getCronogramaDiarioId())) {
        throw new RuntimeException("La asistencia ya fue registrada");
    }

    CronogramaDiario cronograma = cronogramaDiarioRepository.findById(dto.getCronogramaDiarioId())
            .orElseThrow(() -> new RuntimeException("Cronograma diario no encontrado"));

    LocalTime horaInicio = cronograma
            .getCronogramaDocente()
            .getHorarioBloque()
            .getHoraInicio();

    int minutosTardanza = 0;

    if (dto.getHoraEntradaDoc() != null) {

        long minutos = java.time.Duration
                .between(horaInicio, dto.getHoraEntradaDoc())
                .toMinutes();

        minutosTardanza = (int) Math.max(minutos - TOLERANCIA_MINUTOS, 0);
    }

    EstadoAsistenciaDocente estado;

    if (dto.getEstadoAsistencia() == EstadoAsistenciaDocente.PERMISO) {

        estado = EstadoAsistenciaDocente.PERMISO;

    } else if (dto.getHoraEntradaDoc() == null) {

        estado = EstadoAsistenciaDocente.FALTA;

    } else if (minutosTardanza > 0) {

        estado = EstadoAsistenciaDocente.TARDANZA;

    } else {

        estado = EstadoAsistenciaDocente.PUNTUAL;
    }

    AsistenciaDocente asistencia = AsistenciaDocente.builder()
            .horaEntradaDoc(dto.getHoraEntradaDoc())
            .horaSalidaDoc(dto.getHoraSalidaDoc())
            .observacion(dto.getObservacion())
            .materialClase(dto.getMaterialClase())
            .usoTerno(dto.getUsoTerno())
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

    private AsistenciaDocenteResponseDTO mapToResponse(AsistenciaDocente a) {

        return AsistenciaDocenteResponseDTO.builder()
                .id(a.getId())
                .horaEntradaDoc(a.getHoraEntradaDoc())
                .horaSalidaDoc(a.getHoraSalidaDoc())
                .observacion(a.getObservacion())
                .materialClase(a.getMaterialClase())
                .usoTerno(a.getUsoTerno())
                .estadoAsistencia(a.getEstadoAsistencia())
                .cronogramaDiarioId(a.getCronogramaDiario().getId())
                .build();
    }

}