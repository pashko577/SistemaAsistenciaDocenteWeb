package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.actividades_app.model.Entity.Modulo;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.RolModulo;
import com.example.actividades_app.model.dto.Permisos.ModuloResponseDTO;
import com.example.actividades_app.model.dto.Permisos.RolModuloRequestDTO;
import com.example.actividades_app.model.dto.Permisos.RolModuloResponseDTO;
import com.example.actividades_app.repository.ModuloRepository;
import com.example.actividades_app.repository.RolModuloRepository;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.service.RolModuloService;

import jakarta.transaction.Transactional;
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
    // 1. Buscamos el rol actual para verificar su nombre
    Rol rolActual = rolRepository.findById(rolId)
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

    List<Long> idsABuscar = new java.util.ArrayList<>();
    idsABuscar.add(rolId);

    // 2. Si es ADMIN, agregamos los IDs de los roles subordinados
    if ("ADMIN".equals(rolActual.getNombreRol()) || "ROLE_ADMIN".equals(rolActual.getNombreRol())) {
        // Buscamos los otros roles por nombre y añadimos sus IDs
        rolRepository.findAll().stream()
            .filter(r -> r.getNombreRol().contains("ADMINISTRATIVO") || r.getNombreRol().contains("DOCENTE"))
            .forEach(r -> idsABuscar.add(r.getId()));
    }

    // 3. Consultamos el repositorio con la lista completa de IDs
    return rolModuloRepository.findByRolIdIn(idsABuscar)
            .stream()
            .distinct() // Evitamos duplicados si un módulo está asignado a varios roles
            .map(this::mapToResponse)
            .toList();
}

        @Override
    @Transactional // Requerido para operaciones de borrado
    public void desasignarModulo(Long rolId, Long moduloId) {
        // Verificamos si existe antes de intentar borrar
        if (!rolModuloRepository.existsByRolIdAndModuloId(rolId, moduloId)) {
            throw new RuntimeException("La asignación de permiso no existe");
        }
        
        rolModuloRepository.deleteByRolIdAndModuloId(rolId, moduloId);
    }
    
   private RolModuloResponseDTO mapToResponse(RolModulo rm) {
    return RolModuloResponseDTO.builder()
            .id(rm.getId())
            .rolId(rm.getRol().getId())
            .rolNombre(rm.getRol().getNombreRol())
            .modulo(ModuloResponseDTO.builder()
                    .id(rm.getModulo().getId())
                    .nombre(rm.getModulo().getNombre())
                    .ruta(rm.getModulo().getRuta())
                    .descripcion(rm.getModulo().getDescripcion())
                    .icono(rm.getModulo().getIcono()) // Asegúrate de que este campo exista en tu entidad Modulo
                    .build())
            .build();
}


}