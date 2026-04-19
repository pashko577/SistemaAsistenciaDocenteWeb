package com.example.actividades_app.service.Impl;

import com.example.actividades_app.model.Entity.Grado;
import com.example.actividades_app.model.Entity.Seccion;
import com.example.actividades_app.model.dto.ModuloCurso.SeccionRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.SeccionResponseDTO;
import com.example.actividades_app.repository.GradoRepository;
import com.example.actividades_app.repository.SeccionRepository;
import com.example.actividades_app.service.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionServiceImpl implements SeccionService {

    private final SeccionRepository seccionRepository;
    private final GradoRepository gradoRepository;

    @Override
    @Transactional
    public SeccionResponseDTO crear(SeccionRequestDTO dto) {
        validarGradoYSeccion(dto.getNomSeccion(), dto.getGradoId());

        // Usamos getReferenceById si solo necesitamos la entidad para la relación (más
        // eficiente)
        Grado grado = gradoRepository.findById(dto.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        Seccion seccion = Seccion.builder()
                .nomSeccion(dto.getNomSeccion())
                .grado(grado)
                .build();

        return mapToResponse(seccionRepository.save(seccion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeccionResponseDTO> listar() {
        return seccionRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public SeccionResponseDTO obtenerPorId(Long id) {
        return seccionRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));
    }

    @Override
    @Transactional
    public SeccionResponseDTO actualizar(Long id, SeccionRequestDTO dto) {
        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        // Validar si el grado cambió o si el nombre se repite en el mismo grado
        if (!seccion.getGrado().getId().equals(dto.getGradoId()) ||
                !seccion.getNomSeccion().equals(dto.getNomSeccion())) {
            validarGradoYSeccion(dto.getNomSeccion(), dto.getGradoId());
        }

        Grado grado = gradoRepository.findById(dto.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        seccion.setNomSeccion(dto.getNomSeccion());
        seccion.setGrado(grado);

        return mapToResponse(seccionRepository.save(seccion));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!seccionRepository.existsById(id)) {
            throw new RuntimeException("Sección no encontrada");
        }
        seccionRepository.deleteById(id);
    }

    // --- MÉTODOS DE APOYO (OPTIMIZACIÓN) ---

    private void validarGradoYSeccion(String nombre, Long gradoId) {
        if (!gradoRepository.existsById(gradoId)) {
            throw new RuntimeException("El grado no existe");
        }
        if (seccionRepository.existsByNomSeccionAndGradoId(nombre, gradoId)) {
            throw new RuntimeException("La sección '" + nombre + "' ya existe en este grado");
        }
    }

    private SeccionResponseDTO mapToResponse(Seccion s) {
        return SeccionResponseDTO.builder()
                .id(s.getId())
                .nomSeccion(s.getNomSeccion())
                .gradoId(s.getGrado().getId())
                .gradoNombre(s.getGrado().getNumGrado())
                .nivelId(s.getGrado().getNivel().getId())
                .nivelNombre(s.getGrado().getNivel().getNomNivel())
                .build();
    }
}