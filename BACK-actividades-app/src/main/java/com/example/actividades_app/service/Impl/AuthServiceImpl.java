package com.example.actividades_app.service.Impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.actividades_app.config.JwtUtils;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.ModuloUsuario.AuthResponseDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.LoginRequestDTO;
import com.example.actividades_app.model.dto.ModuloUsuario.RegisterRequestDTO;
import com.example.actividades_app.repository.PersonaRepository;
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

        /*
         * @Override
         * public void register(RegisterRequestDTO request) {
         * // 1. Verificar si ya existe el usuario por DNI
         * if (usuarioRepository.existsByPersonaDni(request.getDni())) {
         * throw new RuntimeException("DNI_ALREADY_REGISTERED");
         * }
         * 
         * // 2. Buscar o crear la Persona (necesitas PersonaRepository)
         * Persona persona = personaRepository.findByDni(request.getDni())
         * .orElseThrow(() -> new RuntimeException("PERSONA_NOT_FOUND"));
         * 
         * // Sede
         * Sede sede = sedeRepository.findById(request.getSedeId())
         * .orElseThrow(() -> new RuntimeException("SEDE_NO_ENCONTRADA"));
         * 
         * // 3. Mapear roles
         * Set<Rol> roles = request.getRoles().stream()
         * .map(roleName -> rolRepository.findByNombreRol(roleName)
         * .orElseThrow(() -> new RuntimeException("ROLE_NOT_FOUND")))
         * .collect(Collectors.toSet());
         * 
         * // 4. Construir Usuario
         * Usuario usuario = Usuario.builder()
         * .persona(persona)
         * .password(passwordEncoder.encode(request.getPassword()))
         * .roles(roles)
         * .sede(sede)
         * .build();
         * 
         * usuarioRepository.save(usuario);
         * }
         */

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

        @Override
        @Transactional
        public AuthResponseDTO login(LoginRequestDTO request) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getDni(),
                                                request.getPassword()));

                String username = authentication.getName();

                List<String> roles = authentication.getAuthorities()
                                .stream()
                                .map(grantedAuthority -> grantedAuthority.getAuthority())
                                .toList();
                String token = jwtUtils.generateAccessToken(username, roles);

                return new AuthResponseDTO(token, username, roles);

        }

}
