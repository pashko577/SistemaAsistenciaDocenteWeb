package com.example.actividades_app.service.Impl;

import com.example.actividades_app.model.Entity.*;
import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.AsignacionDocenteResponseDTO;
import com.example.actividades_app.repository.*;
import com.example.actividades_app.service.AsignacionDocenteService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AsignacionDocenteServiceImpl implements AsignacionDocenteService {

    private final AsignacionDocenteRepository asignacionRepository;
    private final DocenteRepository docenteRepository;
    private final ClaseRepository claseRepository;
    private final TipoActividadRepository tipoActividadRepository;

    @Override
    public AsignacionDocenteResponseDTO registrar(AsignacionDocenteRequestDTO dto) {

        Docente docente = docenteRepository.findById(dto.getDocenteId())
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

        Clase clase = claseRepository.findById(dto.getClaseId())
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        TipoActividad tipoActividad = tipoActividadRepository.findById(dto.getTipoActividadId())
                .orElseThrow(() -> new RuntimeException("TipoActividad no encontrado"));

        AsignacionDocente asignacion = AsignacionDocente.builder()
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .docente(docente)
                .clase(clase)
                .tipoActividad(tipoActividad)
                .build();

        asignacionRepository.save(asignacion);

        return mapToResponse(asignacion);
    }

    @Override
    public AsignacionDocenteResponseDTO obtenerPorId(Long id) {

        AsignacionDocente asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        return mapToResponse(asignacion);
    }

    @Override
    public List<AsignacionDocenteResponseDTO> listar() {

        return asignacionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AsignacionDocenteResponseDTO actualizar(Long id, AsignacionDocenteRequestDTO dto) {

        AsignacionDocente asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));

        asignacion.setEstado(dto.getEstado());
        asignacion.setObservaciones(dto.getObservaciones());

        asignacionRepository.save(asignacion);

        return mapToResponse(asignacion);
    }

    @Override
    public void eliminar(Long id) {
        asignacionRepository.deleteById(id);
    }

    private AsignacionDocenteResponseDTO mapToResponse(AsignacionDocente a) {

        return AsignacionDocenteResponseDTO.builder()
                .id(a.getId())
                .estado(a.getEstado())
                .observaciones(a.getObservaciones())
                .docenteId(a.getDocente().getId())
                .docenteNombre(a.getDocente().getUsuario().getPersona().getNombres())
                .claseId(a.getClase().getId())
                .tipoActividadId(a.getTipoActividad().getId())
                .tipoActividadNombre(a.getTipoActividad().getNombre())
                .build();
    }
}