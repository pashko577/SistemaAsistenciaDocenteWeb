package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Modulo;
import com.example.actividades_app.model.dto.Permisos.ModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.ModuloResponseDTO;
import com.example.actividades_app.repository.ModuloRepository;
import com.example.actividades_app.service.ModuloService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;

    @Override
    public ModuloResponseDTO crear(ModuloRequestDTO dto) {

        if (moduloRepository.existsByNombre(dto.getNombre())) {
            throw new RuntimeException("El módulo ya existe");
        }

        Modulo modulo = Modulo.builder()
                .nombre(dto.getNombre())
                .descripcion(dto.getDescripcion())
                .ruta(dto.getRuta())
                .build();

        moduloRepository.save(modulo);

        return mapToResponse(modulo);
    }

    @Override
    public List<ModuloResponseDTO> listar() {
        return moduloRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ModuloResponseDTO obtenerPorId(Long id) {

        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        return mapToResponse(modulo);
    }

    @Override
    public void eliminar(Long id) {

        Modulo modulo = moduloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado"));

        moduloRepository.delete(modulo);
    }

    private ModuloResponseDTO mapToResponse(Modulo modulo) {

        return ModuloResponseDTO.builder()
                .id(modulo.getId())
                .nombre(modulo.getNombre())
                .descripcion(modulo.getDescripcion())
                .ruta(modulo.getRuta())
                .build();
    }
}