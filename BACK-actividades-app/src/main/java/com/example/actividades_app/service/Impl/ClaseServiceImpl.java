package com.example.actividades_app.service.Impl;

import com.example.actividades_app.model.Entity.Clase;
import com.example.actividades_app.model.Entity.Curso;
import com.example.actividades_app.model.Entity.Seccion;
import com.example.actividades_app.model.Entity.PeriodoAcademico;
import com.example.actividades_app.model.dto.ModuloDocente.ClaseRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.ClaseResponseDTO;
import com.example.actividades_app.repository.ClaseRepository;
import com.example.actividades_app.repository.CursoRepository;
import com.example.actividades_app.repository.SeccionRepository;
import com.example.actividades_app.repository.PeriodoAcademicoRepository;
import com.example.actividades_app.service.ClaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClaseServiceImpl implements ClaseService {

    private final ClaseRepository claseRepository;
    private final CursoRepository cursoRepository;
    private final SeccionRepository seccionRepository;
    private final PeriodoAcademicoRepository periodoRepository;

    // ===========================
    // CREAR CLASE (sin validación de duplicado)
    // ===========================
    @Override
    public ClaseResponseDTO crear(ClaseRequestDTO dto) {

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Seccion seccion = seccionRepository.findById(dto.getSeccionId())
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        PeriodoAcademico periodo = periodoRepository.findById(dto.getPeriodoAcademicoId())
                .orElseThrow(() -> new RuntimeException("Periodo académico no encontrado"));

        Clase clase = Clase.builder()
                .tiempoClase(dto.getTiempoClase())
                .aula(dto.getAula())
                .curso(curso)
                .seccion(seccion)
                .periodoAcademico(periodo)
                .build();

        claseRepository.save(clase);

        return mapToResponse(clase);
    }

    // ===========================
    // LISTAR TODAS LAS CLASES
    // ===========================
    @Override
    public List<ClaseResponseDTO> listar() {
        return claseRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ===========================
    // OBTENER CLASE POR ID
    // ===========================
    @Override
    public ClaseResponseDTO obtenerPorId(Long id) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        return mapToResponse(clase);
    }

    // ===========================
    // ACTUALIZAR CLASE (sin validación de duplicado)
    // ===========================
    @Override
    public ClaseResponseDTO actualizar(Long id, ClaseRequestDTO dto) {
        Clase clase = claseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clase no encontrada"));

        Curso curso = cursoRepository.findById(dto.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Seccion seccion = seccionRepository.findById(dto.getSeccionId())
                .orElseThrow(() -> new RuntimeException("Sección no encontrada"));

        PeriodoAcademico periodo = periodoRepository.findById(dto.getPeriodoAcademicoId())
                .orElseThrow(() -> new RuntimeException("Periodo académico no encontrado"));

        clase.setTiempoClase(dto.getTiempoClase());
        clase.setAula(dto.getAula());
        clase.setCurso(curso);
        clase.setSeccion(seccion);
        clase.setPeriodoAcademico(periodo);

        claseRepository.save(clase);

        return mapToResponse(clase);
    }

    // ===========================
    // ELIMINAR CLASE
    // ===========================
    @Override
    public void eliminar(Long id) {
        if (!claseRepository.existsById(id)) {
            throw new RuntimeException("Clase no encontrada");
        }
        claseRepository.deleteById(id);
    }

    // ===========================
    // MAPPER DTO
    // ===========================
    private ClaseResponseDTO mapToResponse(Clase clase) {
        return ClaseResponseDTO.builder()
                .id(clase.getId())
                .tiempoClase(clase.getTiempoClase())
        
                .aula(clase.getAula())
                .cursoId(clase.getCurso().getId())
                .cursoNombre(clase.getCurso().getNombreCurso())
                .seccionId(clase.getSeccion().getId())
                .seccionNombre(clase.getSeccion().getNomSeccion())
                .periodoAcademicoId(clase.getPeriodoAcademico().getId())
                .periodoNombre(clase.getPeriodoAcademico().getNombre())
                .build();
    }
}