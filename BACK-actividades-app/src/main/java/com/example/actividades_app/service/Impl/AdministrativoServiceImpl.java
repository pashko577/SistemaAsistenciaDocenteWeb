package com.example.actividades_app.service.Impl;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoResponseDTO;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.repository.CargoAdministrativoRepository;
import com.example.actividades_app.repository.PersonaRepository;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.repository.SedeRepository;
import com.example.actividades_app.repository.TipoDocumentoRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.AdministrativoService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdministrativoServiceImpl implements AdministrativoService {

        private final AdministrativoRepository administrativoRepository;
        private final UsuarioRepository usuarioRepository;
        private final SedeRepository sedeRepository;
        private final TipoDocumentoRepository tipoDocumentoRepository;
        private final CargoAdministrativoRepository cargoAdministrativoRepository;
        private final PersonaRepository personaRepository;
        private final RolRepository rolRepository;
        private final PasswordEncoder passwordEncoder;

        @Override
        @Transactional
        public AdministrativoResponseDTO registrarAdministrativo(AdministrativoRequestDTO request) {
                System.out.println("DATOS RECIBIDOS: " + request.toString());
                // 1. Validaciones
                if (usuarioRepository.existsByPersonaDni(request.getDni())) {
                        throw new RuntimeException("Ya existe un usuario registrado con ese DNI");
                }

                if (personaRepository.existsByEmail(request.getEmail())) {
                        throw new RuntimeException("El EMAIL se encuentra registrado a otro usuario");
                }

                // 2. Obtener relaciones
                Sede sede = sedeRepository.findById(request.getSedeId())
                                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

                TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

                CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository
                                .findById(request.getCargoAdministrativoId())
                                .orElseThrow(() -> new RuntimeException("Cargo Administrativo no encontrado"));

                // 3. Crear Persona
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

                // 4. Rol
                Rol rolAdministrativo = rolRepository.findByNombreRol("ADMINISTRATIVO")
                                .orElseThrow(() -> new RuntimeException("El rol ADMINISTRATIVO no existe"));

                // 5. Usuario
                Usuario usuario = Usuario.builder()
                                .persona(persona)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(Set.of(rolAdministrativo))
                                .sede(sede)
                                .build();

                usuarioRepository.save(usuario);

                // 6. Administrativo
                Administrativo administrativo = Administrativo.builder()
                                .usuario(usuario)
                                .cargoAdministrativo(cargoAdministrativo)
                                // CAMBIA ESTA LÍNEA:
                                .estado(request.getEstado() != null ? request.getEstado() : Estado.NUEVO)
                                .build();

                Administrativo adminGuardado = administrativoRepository.save(administrativo);

                // 7. MAPEAR A RESPONSE DTO
                return mapToResponse(adminGuardado);
        }

        @Override
        @Transactional
        public AdministrativoResponseDTO actualizarAdministrativo(Long id, AdministrativoRequestDTO request) {
                // 1. Buscar el Administrativo existente
                Administrativo admin = administrativoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

                Usuario usuario = admin.getUsuario();
                Persona persona = usuario.getPersona();

                // 2. Actualizar Persona
                persona.setNombres(request.getNombres());
                persona.setApellidos(request.getApellidos());
                persona.setCelular(request.getCelular());
                persona.setEmail(request.getEmail());
                persona.setDireccion(request.getDireccion());
                // Actualizar TipoDocumento si es necesario
                if (!persona.getTipoDocumento().getId().equals(request.getTipoDocumentoId())) {
                        TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                                        .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));
                        persona.setTipoDocumento(tipoDoc);
                }
                personaRepository.save(persona);

                // 3. Actualizar Usuario (Sede y Password opcional)
                Sede sede = sedeRepository.findById(request.getSedeId())
                                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));
                usuario.setSede(sede);

                if (request.getPassword() != null && !request.getPassword().isBlank()) {
                        usuario.setPassword(passwordEncoder.encode(request.getPassword()));
                }
                usuarioRepository.save(usuario);

                // 4. Actualizar Administrativo (Cargo y Estado)
                CargoAdministrativo cargo = cargoAdministrativoRepository.findById(request.getCargoAdministrativoId())
                                .orElseThrow(() -> new RuntimeException("Cargo no encontrado"));

                admin.setCargoAdministrativo(cargo);
                admin.setEstado(request.getEstado());

                return mapToResponse(administrativoRepository.save(admin));
        }

        @Override
        @Transactional
        public void eliminarAdministrativo(Long id) {
                Administrativo admin = administrativoRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

                // 1. Inactivar al administrativo
                admin.setEstado(Estado.INACTIVO);

                // 2. Opcional: Si el Usuario también tiene un campo estado, inactívalo aquí
                // admin.getUsuario().setActivo(false);

                administrativoRepository.save(admin);
        }

        @Override
        public List<AdministrativoResponseDTO> listarAdministrativos() {
                // Cambia findByEstadoNot por findAll() para enviar toda la data
                return administrativoRepository.findAll()
                                .stream()
                                .map(this::mapToResponse)
                                .toList();
        }

        @Override
        public List<AdministrativoResponseDTO> listarSoloConContrato() {
                return administrativoRepository.findByHasActiveContrato()
                                .stream()
                                .map(this::mapToResponse) // Usamos tu mapper ya existente
                                .toList();
        }

        private AdministrativoResponseDTO mapToResponse(Administrativo admin) {
                // Extraemos la persona para facilitar el acceso a los datos
                Usuario usuario = admin.getUsuario();
                Persona persona = admin.getUsuario().getPersona();
                Sede sede = admin.getUsuario().getSede();

                Long contratoIdActivo = (usuario.getContratos() != null) ? usuario.getContratos().stream()
                                .filter(c -> c.getEstado() != null &&
                                                "ACTIVO".equalsIgnoreCase(c.getEstado().toString().trim()))
                                .map(Contrato::getId)
                                .findFirst()
                                .orElse(null) : null;
                return AdministrativoResponseDTO.builder()
                                .id(admin.getId())
                                .usuarioId(admin.getUsuario().getId())
                                .contratoId(contratoIdActivo)

                                // Llenamos nombres y apellidos por separado
                                .nombres(persona.getNombres())
                                .apellidos(persona.getApellidos())

                                // Nombre completo para el buscador o etiquetas
                                .nombreUsuario(persona.getNombres() + " " + persona.getApellidos())

                                .cargoAdministrativoId(admin.getCargoAdministrativo().getId())
                                .nombreCargo(admin.getCargoAdministrativo().getNombreCargo())
                                .estado(admin.getEstado())

                                // Datos de contacto
                                .dni(persona.getDni())
                                .email(persona.getEmail())
                                .celular(persona.getCelular())
                                .direccion(persona.getDireccion())

                                // Datos de la sede
                                .sedeId(sede != null ? sede.getId() : null)
                                .nombreSede(sede != null ? sede.getNombreSede() : "Sin Sede")
                                .build();
        }
}