package com.example.actividades_app.service.Impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.EspecialidadDocenteResponseDTO;
import com.example.actividades_app.repository.EspecialidadDocenteRepository;
import com.example.actividades_app.service.EspecialidadDocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class EspecialidadDocenteServiceImpl implements EspecialidadDocenteService {

    private final EspecialidadDocenteRepository especialidadDocenteRepository;

    @Override
    public EspecialidadDocenteResponseDTO crearEspecialidadDocente(EspecialidadDocenteRequestDTO dto) {
        if (especialidadDocenteRepository.existsByNombreEspecialidad(dto.getNombreEspecialidad())) {
            throw new RuntimeException("La especialidad ya existe");
        }

        EspecialidadDocente especialidadDocente = EspecialidadDocente.builder()
                .nombreEspecialidad(dto.getNombreEspecialidad())
                .build();

        EspecialidadDocente guardado = especialidadDocenteRepository.save(especialidadDocente);
        return mapToResponseDTO(guardado);
    }

    @Override
    public EspecialidadDocenteResponseDTO actualizarEspecialidadDocente(Long id, String nombreEspecialidad) {
        EspecialidadDocente especialidadDocente = especialidadDocenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        
        especialidadDocente.setNombreEspecialidad(nombreEspecialidad);
        EspecialidadDocente actualizado = especialidadDocenteRepository.save(especialidadDocente);
        
        return mapToResponseDTO(actualizado);
    }

    @Override
    public void eliminarEspecilidadDocente(Long id) {
        EspecialidadDocente especialidadDocente = especialidadDocenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        
        especialidadDocenteRepository.delete(especialidadDocente);
    }

    @Override
    public EspecialidadDocenteResponseDTO obtenerPorId(Long id) {
        EspecialidadDocente especialidadDocente = especialidadDocenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        
        return mapToResponseDTO(especialidadDocente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EspecialidadDocenteResponseDTO> listarEspecialidadDocente() {
        return especialidadDocenteRepository.findAll()
                .stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Mapea la Entidad al DTO de respuesta para Angular.
     * Esto resuelve el problema de nombres de campos y recursión.
     */
    private EspecialidadDocenteResponseDTO mapToResponseDTO(EspecialidadDocente entity) {
        return EspecialidadDocenteResponseDTO.builder()
                .especialidadDocenteId(entity.getId()) // Mapea 'id' de BD a 'especialidadDocenteId' de Angular
                .nombreEspecialidad(entity.getNombreEspecialidad())
                .build();
    }
}