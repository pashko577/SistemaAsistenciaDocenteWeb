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

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeccionServiceImpl implements SeccionService {

    private final SeccionRepository seccionRepository;
    private final GradoRepository gradoRepository;

    @Override
    public SeccionResponseDTO crear(SeccionRequestDTO dto) {

        if (!gradoRepository.existsById(dto.getGradoId())) {
            throw new RuntimeException("El grado no existe");
        }

        if (seccionRepository.existsByNomSeccionAndGradoId(dto.getNomSeccion(), dto.getGradoId())) {
            throw new RuntimeException("La sección ya existe en ese grado");
        }

        Grado grado = gradoRepository.findById(dto.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        Seccion seccion = Seccion.builder()
                .nomSeccion(dto.getNomSeccion())
                .grado(grado)
                .build();

        seccionRepository.save(seccion);

        return SeccionResponseDTO.builder()
                .id(seccion.getId())
                .nomSeccion(seccion.getNomSeccion())
                .gradoId(grado.getId())
                .gradoNombre(grado.getNumGrado())
                .build();
    }

    @Override
    public List<SeccionResponseDTO> listar() {

        return seccionRepository.findAll()
                .stream()
                .map(s -> SeccionResponseDTO.builder()
                        .id(s.getId())
                        .nomSeccion(s.getNomSeccion())
                        .gradoId(s.getGrado().getId())
                        .gradoNombre(s.getGrado().getNumGrado())
                        .build())
                .toList();
    }

    @Override
    public SeccionResponseDTO obtenerPorId(Long id) {

        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        return SeccionResponseDTO.builder()
                .id(seccion.getId())
                .nomSeccion(seccion.getNomSeccion())
                .gradoId(seccion.getGrado().getId())
                .gradoNombre(seccion.getGrado().getNumGrado())
                .build();
    }

    @Override
    public SeccionResponseDTO actualizar(Long id, SeccionRequestDTO dto) {

        Seccion seccion = seccionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        if (!gradoRepository.existsById(dto.getGradoId())) {
            throw new RuntimeException("El grado no existe");
        }

        if (seccionRepository.existsByNomSeccionAndGradoId(dto.getNomSeccion(), dto.getGradoId())) {
            throw new RuntimeException("La sección ya existe en ese grado");
        }

        Grado grado = gradoRepository.findById(dto.getGradoId())
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        seccion.setNomSeccion(dto.getNomSeccion());
        seccion.setGrado(grado);

        seccionRepository.save(seccion);

        return SeccionResponseDTO.builder()
                .id(seccion.getId())
                .nomSeccion(seccion.getNomSeccion())
                .gradoId(grado.getId())
                .gradoNombre(grado.getNumGrado())
                .build();
    }

    @Override
    public void eliminar(Long id) {

        if (!seccionRepository.existsById(id)) {
            throw new RuntimeException("Sección no encontrada");
        }

        seccionRepository.deleteById(id);
    }
}