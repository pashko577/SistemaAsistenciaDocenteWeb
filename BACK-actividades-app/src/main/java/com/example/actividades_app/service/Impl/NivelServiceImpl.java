package com.example.actividades_app.service.Impl;


import com.example.actividades_app.model.Entity.Nivel;
import com.example.actividades_app.model.dto.ModuloCurso.NivelRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.NivelResponseDTO;
import com.example.actividades_app.repository.NivelRepository;
import com.example.actividades_app.service.NivelService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NivelServiceImpl implements NivelService {

    private final NivelRepository nivelRepository;

    @Override
    public NivelResponseDTO crear(NivelRequestDTO dto) {

        if(nivelRepository.existsByNomNivel(dto.getNomNivel())){
            throw new RuntimeException("El nivel ya existe");
        }

        Nivel nivel = Nivel.builder()
                .nomNivel(dto.getNomNivel())
                .build();

        nivelRepository.save(nivel);

        return NivelResponseDTO.builder()
                .id(nivel.getId())
                .nomNivel(nivel.getNomNivel())
                .build();
    }

    @Override
    public List<NivelResponseDTO> listar() {

        return nivelRepository.findAll()
                .stream()
                .map(n -> NivelResponseDTO.builder()
                        .id(n.getId())
                        .nomNivel(n.getNomNivel())
                        .build())
                .toList();
    }

    @Override
    public NivelResponseDTO obtenerPorId(Long id) {

        Nivel nivel = nivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        return NivelResponseDTO.builder()
                .id(nivel.getId())
                .nomNivel(nivel.getNomNivel())
                .build();
    }

    @Override
    public NivelResponseDTO actualizar(Long id, NivelRequestDTO dto) {

        Nivel nivel = nivelRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        nivel.setNomNivel(dto.getNomNivel());

        nivelRepository.save(nivel);

        return NivelResponseDTO.builder()
                .id(nivel.getId())
                .nomNivel(nivel.getNomNivel())
                .build();
    }

    @Override
    public void eliminar(Long id) {

        if(!nivelRepository.existsById(id)){
            throw new RuntimeException("Nivel no encontrado");
        }

        nivelRepository.deleteById(id);
    }
}