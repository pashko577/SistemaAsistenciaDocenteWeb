package com.example.actividades_app.service.Impl;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.model.Entity.CargoAdministrativo;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.Sede;
import com.example.actividades_app.model.Entity.TipoDocumento;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoRequestDTO;
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
        public void registrarAdministrativo(AdministrativoRequestDTO request){
                // 1. Validar si ya existe el usuario por DNI
                if (usuarioRepository.existsByPersonaDni(request.getDni())) {
                        throw new RuntimeException("Ya existe un usuario registrado con ese DNI");
                }

                if (personaRepository.existsByEmail(request.getEmail())) {
                      throw new RuntimeException("El EMAIL se encuentra registrado a otro usuario");  
                }

                // 2. Obtener entidades relacionales
                Sede sede = sedeRepository.findById(request.getSedeId())
                                .orElseThrow(() -> new RuntimeException("Sede no encontrada"));

                TipoDocumento tipoDoc = tipoDocumentoRepository.findById(request.getTipoDocumentoId())
                                .orElseThrow(() -> new RuntimeException("Tipo de documento no encontrado"));

                CargoAdministrativo cargoAdministrativo = cargoAdministrativoRepository.findById(request.getCargoAdministrativoID())
                                .orElseThrow(() -> new RuntimeException("Cargo Administrativo no encontrado"));


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
                Rol rolAdministrativo = rolRepository.findByNombreRol("ADMINISTRATIVO")
                                .orElseThrow(() -> new RuntimeException(
                                                "El rol ADMINISTRATIVO no existe en la base de datos"));

                Usuario usuario = Usuario.builder()
                                .persona(persona)
                                .password(passwordEncoder.encode(request.getPassword()))
                                .roles(Set.of(rolAdministrativo))
                                .sede(sede)
                                .build();
                usuarioRepository.save(usuario);

                // 5. Crear y persistir Docente
                Administrativo administrativo = Administrativo.builder()
                                .usuario(usuario)
                                .cargoAdministrativo(cargoAdministrativo)
                                .estado(Estado.NUEVO)
                                .build();
                administrativoRepository.save(administrativo);
        }
}
