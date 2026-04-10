package com.example.actividades_app.service.Impl;

import com.example.actividades_app.service.RolService;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.dto.ModuloUsuario.RegistrarRolRequestDTO;
import com.example.actividades_app.config.exception.RoleNotFoundException;
import com.example.actividades_app.config.exception.RoleAlreadyExistsException;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor; // 1. Importa esto

import java.util.Optional;
import java.util.List;

@Service
@RequiredArgsConstructor // 2. Genera el constructor para campos 'final'
public class RolServiceImpl implements RolService {

    // 3. Quita @Autowired. Al ser final, @RequiredArgsConstructor lo inyectará.
    private final RolRepository rolRepository;

    @Override
    public Rol registrarRol(RegistrarRolRequestDTO dto) {
        if (rolRepository.existsByNombreRol(dto.getName())) {
            throw new RoleAlreadyExistsException("El rol ya existe");
        }

        Rol rol = new Rol();
        rol.setNombreRol(dto.getName());
        return rolRepository.save(rol);
    }

    @Override
    public Optional<Rol> buscarPorNombre(String name) {
        return rolRepository.findByNombreRol(name);
    }

    @Override
    public List<Rol> obtenerTodosLosRoles() {
        return rolRepository.findAll();
    }

    @Override
    public void eliminarRol(Long id) {
        Rol rol = rolRepository.findById(id)
                .orElseThrow(() -> new RoleNotFoundException("El rol con ID " + id + " no existe"));
        rolRepository.delete(rol);
    }
}