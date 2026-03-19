package com.example.actividades_app.service.Impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.*;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteResponseDTO;
import com.example.actividades_app.repository.*;
import com.example.actividades_app.service.DocenteService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocenteServiceImpl implements DocenteService {

    private final DocenteRepository docenteRepository;
    private final EspecialidadDocenteRepository especialidadDocenteRepository;
    private final UsuarioRepository usuarioRepository;
    private final SedeRepository sedeRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final PersonaRepository personaRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public DocenteResponseDTO registerDocente(DocenteRequestDTO request) {
        // 1. Validaciones
        if (usuarioRepository.existsByPersonaDni(request.getDni())) {
            throw new RuntimeException("El DNI ya se encuentra registrado");
        }
        if (personaRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El EMAIL ya se encuentra registrado a otro usuario");
        }

        // 2. Obtener Relaciones
        Sede sede = sedeRepository.findById(request.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
        EspecialidadDocente especialidad = especialidadDocenteRepository.findById(request.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

        // 3. Persona
        Persona persona = Persona.builder()
                .dni(request.getDni())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .celular(request.getCelular())
                .email(request.getEmail())
                .direccion(request.getDireccion())
                .tipoDocumento(tipoDoc)
                .build();
        personaRepository.save(persona);

        // 4. Usuario
        Rol rolDocente = rolRepository.findByNombreRol("DOCENTE")
                .orElseThrow(() -> new RuntimeException("Error: El rol DOCENTE no existe"));

        Usuario usuario = Usuario.builder()
                .persona(persona)
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(rolDocente))
                .sede(sede)
                .build();
        usuarioRepository.save(usuario);

        // 5. Docente
        Docente docente = Docente.builder()
                .usuario(usuario)
                .especialidadDocente(especialidad)
                .observaciones(request.getObservaciones())
                .estado(Estado.NUEVO)
                .build();
        
        return convertirAResponse(docenteRepository.save(docente));
    }

    @Override
    @Transactional
    public DocenteResponseDTO actualizarDocente(Long id, DocenteRequestDTO request) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Docente no encontrado"));
        
        Usuario usuario = docente.getUsuario();
        Persona persona = usuario.getPersona();

        // Validar Email si cambió
        if (!persona.getEmail().equals(request.getEmail()) && personaRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El nuevo Email ya está en uso");
        }

        // Actualizar Persona
        persona.setNombres(request.getNombres());
        persona.setApellidos(request.getApellidos());
        persona.setCelular(request.getCelular());
        persona.setEmail(request.getEmail());
        persona.setDireccion(request.getDireccion());

        // Actualizar Usuario (Sede y Password opcional)
        Sede sede = sedeRepository.findById(request.getSedeId())
                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
        usuario.setSede(sede);
        
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            usuario.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Actualizar Docente
        EspecialidadDocente especialidad = especialidadDocenteRepository.findById(request.getEspecialidadId())
                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));
        
        docente.setEspecialidadDocente(especialidad);
        docente.setObservaciones(request.getObservaciones());

        // --- AGREGA ESTA LÍNEA AQUÍ ---
    if (request.getEstado() != null) {
        docente.setEstado(request.getEstado());
    }

        return convertirAResponse(docenteRepository.save(docente));
    }

   @Override
@Transactional
public void eliminarDocente(Long id) {
    Docente docente = docenteRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Docente no encontrado"));

    // Borrado lógico
    docente.setEstado(Estado.INACTIVO);
    docenteRepository.save(docente);
}

    @Override
    @Transactional(readOnly = true)
    public List<DocenteResponseDTO> listarTodos() {
        return docenteRepository.findAll().stream()
                .map(this::convertirAResponse)
                .collect(Collectors.toList());
    }

   private DocenteResponseDTO convertirAResponse(Docente docente) {
    Persona p = docente.getUsuario().getPersona();
    return DocenteResponseDTO.builder()
            .id(docente.getId())
            .dni(p.getDni())
            // --- NUEVOS CAMPOS ---
            .sedeId(docente.getUsuario().getSede().getId())
            .nombreSede(docente.getUsuario().getSede().getNombreSede())
            .especialidadId(docente.getEspecialidadDocente().getId())
            // ---------------------
            .nombres(p.getNombres())
            .apellidos(p.getApellidos())
            .email(p.getEmail())
            .celular(p.getCelular())
            .direccion(p.getDireccion())
            
            .nombreEspecialidad(docente.getEspecialidadDocente().getNombreEspecialidad())
            .tipoDocumentoNombre(p.getTipoDocumento().getNombreTD())
            .estado(docente.getEstado().name())
            .observaciones(docente.getObservaciones())
            .build();
}
}