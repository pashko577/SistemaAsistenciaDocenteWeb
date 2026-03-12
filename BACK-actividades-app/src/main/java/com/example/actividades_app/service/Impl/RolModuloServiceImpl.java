package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Modulo;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.RolModulo;
import com.example.actividades_app.model.dto.Permisos.RolModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.RolModuloResponseDTO;
import com.example.actividades_app.repository.ModuloRepository;
import com.example.actividades_app.repository.RolModuloRepository;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.service.RolModuloService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RolModuloServiceImpl implements RolModuloService {

    private final RolModuloRepository rolModuloRepository;
    private final RolRepository rolRepository;
    private final ModuloRepository moduloRepository;

    @Override
    public RolModuloResponseDTO asignarModulo(RolModuloRequestDTO dto) {

        if (rolModuloRepository.existsByRolIdAndModuloId(dto.getRolId(), dto.getModuloId())) {
            throw new RuntimeException("El módulo ya está asignado a este rol");
        }

        Rol rol = rolRepository.findById(dto.getRolId())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        Modulo modulo = moduloRepository.findById(dto.getModuloId())
                .orElseThrow(() -> new RuntimeException("Modulo no encontrado"));

        RolModulo rolModulo = RolModulo.builder()
                .rol(rol)
                .modulo(modulo)
                .build();

        rolModuloRepository.save(rolModulo);

        return mapToResponse(rolModulo);
    }

    @Override
    public List<RolModuloResponseDTO> listarPorRol(Long rolId) {

        return rolModuloRepository.findByRolId(rolId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private RolModuloResponseDTO mapToResponse(RolModulo rm) {

        return RolModuloResponseDTO.builder()
                .id(rm.getId())
                .rolId(rm.getRol().getId())
                .rolNombre(rm.getRol().getNombreRol())
                .moduloId(rm.getModulo().getId())
                .moduloNombre(rm.getModulo().getNombre())
                .build();
    }
}