package com.example.actividades_app.service.Impl;


import com.example.actividades_app.model.Entity.Grado;
import com.example.actividades_app.model.Entity.Nivel;
import com.example.actividades_app.model.dto.ModuloCurso.GradoRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.GradoResponseDTO;
import com.example.actividades_app.repository.GradoRepository;
import com.example.actividades_app.repository.NivelRepository;
import com.example.actividades_app.service.GradoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradoServiceImpl implements GradoService {

    private final GradoRepository gradoRepository;
    private final NivelRepository nivelRepository;

    @Override
    public GradoResponseDTO crear(GradoRequestDTO dto) {

        if (!nivelRepository.existsById(dto.getNivelId())) {
            throw new RuntimeException("El nivel no existe");
        }

        if (gradoRepository.existsByNumGradoAndNivelId(dto.getNumGrado(), dto.getNivelId())) {
            throw new RuntimeException("El grado ya existe en ese nivel");
        }

        Nivel nivel = nivelRepository.findById(dto.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        Grado grado = Grado.builder()
                .numGrado(dto.getNumGrado())
                .nivel(nivel)
                .build();

        gradoRepository.save(grado);

        return GradoResponseDTO.builder()
                .id(grado.getId())
                .numGrado(grado.getNumGrado())
                .nivelId(nivel.getId())
                .nivelNombre(nivel.getNomNivel())
                .build();
    }

    @Override
    public List<GradoResponseDTO> listar() {

        return gradoRepository.findAll()
                .stream()
                .map(g -> GradoResponseDTO.builder()
                        .id(g.getId())
                        .numGrado(g.getNumGrado())
                        .nivelId(g.getNivel().getId())
                        .nivelNombre(g.getNivel().getNomNivel())
                        .build())
                .toList();
    }

    @Override
    public GradoResponseDTO obtenerPorId(Long id) {

        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        return GradoResponseDTO.builder()
                .id(grado.getId())
                .numGrado(grado.getNumGrado())
                .nivelId(grado.getNivel().getId())
                .nivelNombre(grado.getNivel().getNomNivel())
                .build();
    }

    @Override
    public GradoResponseDTO actualizar(Long id, GradoRequestDTO dto) {

        Grado grado = gradoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grado no encontrado"));

        if (!nivelRepository.existsById(dto.getNivelId())) {
            throw new RuntimeException("El nivel no existe");
        }

        if (gradoRepository.existsByNumGradoAndNivelId(dto.getNumGrado(), dto.getNivelId())) {
            throw new RuntimeException("Ese grado ya existe en ese nivel");
        }

        Nivel nivel = nivelRepository.findById(dto.getNivelId())
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        grado.setNumGrado(dto.getNumGrado());
        grado.setNivel(nivel);

        gradoRepository.save(grado);

        return GradoResponseDTO.builder()
                .id(grado.getId())
                .numGrado(grado.getNumGrado())
                .nivelId(nivel.getId())
                .nivelNombre(nivel.getNomNivel())
                .build();
    }

    @Override
    public void eliminar(Long id) {

        if (!gradoRepository.existsById(id)) {
            throw new RuntimeException("Grado no encontrado");
        }

        gradoRepository.deleteById(id);
    }
}