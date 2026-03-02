package com.example.actividades_app.service.Impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Docente;
import com.example.actividades_app.model.Entity.EspecialidadDocente;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteRequestDTO;
import com.example.actividades_app.model.dto.ModuloDocente.DocenteResponseDTO;
import com.example.actividades_app.repository.DocenteRepository;
import com.example.actividades_app.repository.EspecialidadDocenteRepository;
import com.example.actividades_app.repository.PersonaRepository;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.repository.SedeRepository;
import com.example.actividades_app.repository.TipoDocumentoRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.DocenteService;

import jakarta.transaction.Transactional;
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
        public void registerDocente(DocenteRequestDTO request) {

                // 1. Validar si ya existe el usuario por DNI
                if (usuarioRepository.existsByPersonaDni(request.getDni())) {
                        throw new RuntimeException("El DNI ya se encuentra registrado");
                }
                if (personaRepository.existsByEmail(request.getEmail())) {
                      throw new RuntimeException("El EMAIL se encuentra registrado a otro usuario");  
                }
                
                // 2. Obtener entidades relacionales
                Sede sede = sedeRepository.findById(request.getSedeId())
                                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

                TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

                EspecialidadDocente especialidad = especialidadDocenteRepository.findById(request.getEspecialidadId())
                                .orElseThrow(() -> new RuntimeException("Especialidad no encontrada"));

                // 3. Crear y persistir Persona
                Persona persona = Persona.builder()
                                .dni(request.getDni())
                                .nombres(request.getNombres())
                                .apellidos(request.getApellidos())
                                .celular(request.getCelular().toString())
                                .email(request.getEmail())
                                .direccion(request.getDireccion())
                                .tipoDocumento(tipoDoc)
                                .build();
                personaRepository.save(persona);

                // 4. Buscar Rol DOCENTE y crear Usuario
                Rol rolDocente = rolRepository.findByNombreRol("DOCENTE")
                                .orElseThrow(() -> new RuntimeException(
                                                "Error: El rol DOCENTE no existe en la base de datos"));

                Usuario usuario = Usuario.builder()
                                .persona(persona)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(Set.of(rolDocente)) 
                                .sede(sede)
                                .build();
                usuarioRepository.save(usuario);

                // 5. Crear y persistir Docente
                Docente docente = Docente.builder()
                                .usuario(usuario)
                                .especialidadDocente(especialidad)
                                .observaciones(request.getObservaciones())
                                .estado(Estado.NUEVO)
                                .build();
                docenteRepository.save(docente);
        }

        @Override
        @Transactional
        public List<DocenteResponseDTO> listarTodos(){
                return docenteRepository.findAll().stream()
                        .map(this::convertirAResponse)
                        .collect(Collectors.toList());
        }

        private DocenteResponseDTO convertirAResponse(Docente docente) {
        return DocenteResponseDTO.builder()
                .id(docente.getId())
                .dni(docente.getUsuario().getPersona().getDni())
                .nombres(docente.getUsuario().getPersona().getNombres())
                .apellidos(docente.getUsuario().getPersona().getApellidos())
                .email(docente.getUsuario().getPersona().getEmail())
                .celular(docente.getUsuario().getPersona().getCelular())
                .direccion(docente.getUsuario().getPersona().getDireccion())
                .nombreSede(docente.getUsuario().getSede().getNombreSede())
                .nombreEspecialidad(docente.getEspecialidadDocente().getNombreEspecialidad())
                .tipoDocumentoNombre(docente.getUsuario().getPersona().getTipoDocumento().getNombreTD())
                .estado(docente.getEstado().name()) // Convierte el Enum a String
                .observaciones(docente.getObservaciones())
                .build();
    }
}
