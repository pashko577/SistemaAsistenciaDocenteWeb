package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Curso;

import com.example.actividades_app.model.dto.ModuloCurso.CursoRequestDTO;
import com.example.actividades_app.model.dto.ModuloCurso.CursoResponseDTO;
import com.example.actividades_app.repository.CursoRepository;
import com.example.actividades_app.service.CursoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;

    @Override
    public CursoResponseDTO crear(CursoRequestDTO dto) {

        if(cursoRepository.existsByNombreCurso(dto.getNombreCurso())){
            throw new RuntimeException("El curso ya existe");
        }

        Curso curso = Curso.builder()
                .nombreCurso(dto.getNombreCurso())
                .build();

        cursoRepository.save(curso);

        return CursoResponseDTO.builder()
                .id(curso.getId())
                .nombreCurso(curso.getNombreCurso())
                .build();
    }

    @Override
    public List<CursoResponseDTO> listar() {

        return cursoRepository.findAll()
                .stream()
                .map(c -> CursoResponseDTO.builder()
                        .id(c.getId())
                        .nombreCurso(c.getNombreCurso())
                        .build())
                .toList();
    }

    @Override
    public CursoResponseDTO obtenerPorId(Long id) {

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        return CursoResponseDTO.builder()
                .id(curso.getId())
                .nombreCurso(curso.getNombreCurso())
                .build();
    }

    @Override
    public CursoResponseDTO actualizar(Long id, CursoRequestDTO dto) {

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nivel no encontrado"));

        curso.setNombreCurso(dto.getNombreCurso());

        cursoRepository.save(curso);

        return CursoResponseDTO.builder()
                .id(curso.getId())
                .nombreCurso(curso.getNombreCurso())
                .build();
    }

    @Override
    public void eliminar(Long id) {

        if(!cursoRepository.existsById(id)){
            throw new RuntimeException("Nivel no encontrado");
        }

        cursoRepository.deleteById(id);
    }
}