package com.example.actividades_app.service.Impl;

import com.example.actividades_app.enums.EstadoAdelanto;
import com.example.actividades_app.model.Entity.Adelanto;
import com.example.actividades_app.model.Entity.Pago;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.Entity.Persona;
import com.example.actividades_app.model.dto.Reporte.AdelantoRequestDTO;
import com.example.actividades_app.model.dto.Reporte.AdelantoResponseDTO;
import com.example.actividades_app.repository.AdelantoRepository;
import com.example.actividades_app.repository.PagoRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.AdelantoService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdelantoServiceImpl implements AdelantoService {

    private final AdelantoRepository adelantoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PagoRepository pagoRepository;

    @Override
    @Transactional
    public AdelantoResponseDTO registrar(AdelantoRequestDTO request) {
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Adelanto adelanto = Adelanto.builder()
                .nombre(request.getNombre())
                .monto(request.getMonto())
                .estado(EstadoAdelanto.PENDIENTE)
                .usuario(usuario)
                .build();

        return mapToResponse(adelantoRepository.save(adelanto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdelantoResponseDTO> listarPendientesPorUsuario(Long usuarioId) {
        return adelantoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoAdelanto.PENDIENTE)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPendiente(Long usuarioId) {
        return adelantoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoAdelanto.PENDIENTE)
                .stream()
                .map(Adelanto::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

   @Override
@Transactional
public void aplicarAdelantosAPago(Long usuarioId, Long pagoId) {
    // Buscamos el pago real de la base de datos (o un proxy)
    Pago pago = pagoRepository.findById(pagoId)
            .orElseThrow(() -> new RuntimeException("Pago no encontrado"));

    List<Adelanto> pendientes = adelantoRepository.findByUsuarioIdAndEstado(usuarioId, EstadoAdelanto.PENDIENTE);

    pendientes.forEach(a -> {
        a.setEstado(EstadoAdelanto.APLICADO);
        a.setPago(pago); // Ahora sí es un pago que JPA reconoce
    });
    
    // No hace falta saveAll si el método es @Transactional y las entidades vienen del repo,
    // pero ponerlo no hace daño.
    adelantoRepository.saveAll(pendientes);
}

    @Override
    @Transactional
    public void anularAdelanto(Long id) {
        Adelanto adelanto = adelantoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Adelanto no encontrado"));
        
        if (adelanto.getEstado() == EstadoAdelanto.APLICADO) {
            throw new RuntimeException("No se puede anular un adelanto ya cobrado en planilla");
        }
        
        adelanto.setEstado(EstadoAdelanto.ANULADO);
        adelantoRepository.save(adelanto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AdelantoResponseDTO> listarTodos() {
        return adelantoRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ======================
    // MAPPER TO RESPONSE (Siguiendo tu patrón)
    // ======================
    private AdelantoResponseDTO mapToResponse(Adelanto adelanto) {
        // Extraemos las relaciones para facilitar el acceso
        Usuario usuario = adelanto.getUsuario();
        Persona persona = usuario.getPersona();
        Pago pago = adelanto.getPago();

        return AdelantoResponseDTO.builder()
                .id(adelanto.getId())
                .nombre(adelanto.getNombre())
                .monto(adelanto.getMonto())
                .estado(adelanto.getEstado())

                // Datos de Usuario / Persona vinculada
                .usuarioId(usuario.getId())
                .dniPersonal(persona.getDni())
                // Nombre completo para buscadores o tablas
                .nombreCompletoPersonal(persona.getNombres() + " " + persona.getApellidos())

                // Datos del Pago (Solo si ya fue aplicado)
                .pagoId(pago != null ? pago.getId() : null)
                .build();
    }
}