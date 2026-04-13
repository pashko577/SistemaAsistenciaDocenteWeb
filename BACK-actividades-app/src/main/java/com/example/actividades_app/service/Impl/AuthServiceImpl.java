package com.example.actividades_app.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import com.example.actividades_app.config.JwtUtils;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.RolModulo;
import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.ModuloUsuario.AuthResponseDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.LoginRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.RegisterRequestDTO;
import com.example.actividades_app.model.dto.Permisos.ModuloRequestDTO;
import com.example.actividades_app.repository.PersonaRepository;
import com.example.actividades_app.repository.RolModuloRepository;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.repository.SedeRepository;
import com.example.actividades_app.repository.TipoDocumentoRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.AuthService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

        private final UsuarioRepository usuarioRepository;
        private final RolRepository rolRepository;
        private final PasswordEncoder passwordEncoder;
        private final AuthenticationManager authenticationManager;
        private final JwtUtils jwtUtils;
        private final PersonaRepository personaRepository;
        private final SedeRepository sedeRepository;
        private final TipoDocumentoRepository tipoDocumentoRepository;
        private final RolModuloRepository rolModuloRepository;
        @Override
        @Transactional
        public void register(RegisterRequestDTO request) {
                // 1. Validar si el usuario ya existe
                if (usuarioRepository.existsByPersonaDni(request.getDni())) {
                        throw new RuntimeException("EL DNI YA TIENE UN USUARIO ASIGNADO");
                }

                // 2. Buscar Sede y TipoDocumento (Validación previa)
                Sede sede = sedeRepository.findById(request.getSedeId())
                                .orElseThrow(() -> new RuntimeException("SEDE NO ENCONTRADA"));

                TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                                .orElseThrow(() -> new RuntimeException("TIPO DE DOCUMENTO NO ENCONTRADO"));

                // 3. Buscar o Crear Persona
                Persona persona = personaRepository.findByDni(request.getDni())
                                .orElseGet(() -> {
                                        Persona nuevaPersona = Persona.builder()
                                                        .dni(request.getDni())
                                                        .nombres(request.getNombres())
                                                        .apellidos(request.getApellidos())
                                                        .celular(request.getCelular().toString())
                                                        .email(request.getEmail())
                                                        .direccion(request.getDireccion())
                                                        .tipoDocumento(tipoDoc) // Asignamos la entidad encontrada
                                                        .build();
                                        return personaRepository.save(nuevaPersona);
                                });

                // 4. Mapear roles (con protección contra null)
                Set<Rol> roles = (request.getRoles() == null ? new HashSet<String>() : request.getRoles())
                                .stream()
                                .map(roleName -> rolRepository.findByNombreRol(roleName)
                                                .orElseThrow(() -> new RuntimeException(
                                                                "ROL NO ENCONTRADO: " + roleName)))
                                .collect(Collectors.toSet());

                // 5. Crear Usuario
                Usuario usuario = Usuario.builder()
                                .persona(persona)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(roles)
                                .sede(sede)
                                .build();

                usuarioRepository.save(usuario);
        }

// Archivo: AuthServiceImpl.java (Método login)

@Override
public AuthResponseDTO login(LoginRequestDTO request) {
    // 1. Autenticación (Permanece igual)
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getDni(), request.getPassword()));
    
    User userDetails = (User) authentication.getPrincipal();
    String dni = userDetails.getUsername();
    Usuario usuario = usuarioRepository.findByPersonaDni(dni)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    // 2. OBTENER IDS DE ROLES ASIGNADOS AL USUARIO
    // Extraemos solo los IDs de los roles que el usuario tiene en la DB.
    List<Long> rolesIds = usuario.getRoles().stream()
            .map(Rol::getId)
            .collect(Collectors.toList());

    // 3. OBTENER MÓDULOS DESDE LA TABLA DE ASIGNACIÓN (rol_modulo)
    // Aquí es donde se respeta tu pantalla de "Gestión de Accesos".
    List<ModuloRequestDTO> rutasPermitidas = rolModuloRepository.findByRolIdIn(rolesIds).stream()
        .map(RolModulo::getModulo)
        .distinct() // Evita duplicados si el usuario tiene múltiples roles con el mismo módulo
        .map(m -> {
            ModuloRequestDTO dto = new ModuloRequestDTO();
            dto.setNombre(m.getNombre());
            dto.setRuta(m.getRuta());
            dto.setDescripcion(m.getDescripcion());
            dto.setIcono(m.getIcono());
            return dto;
        })
        .collect(Collectors.toList());

    // 4. Generar nombres de roles y Token
    List<String> rolesNames = usuario.getRoles().stream()
            .map(Rol::getNombreRol)
            .toList();
            
    String token = jwtUtils.generateAccessToken(dni, rolesNames);

    return AuthResponseDTO.builder()
            .token(token)
            .dni(dni)
            .roles(rolesNames)
            .rutas_permitidas(rutasPermitidas) 
            .message("Login exitoso")
            .build();
}

}
