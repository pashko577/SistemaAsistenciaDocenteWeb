package com.example.actividades_app.service.Impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.enums.TipoPlanilla;
import com.example.actividades_app.model.Entity.Contrato;
import com.example.actividades_app.model.Entity.Contrato.TipoPago;
import com.example.actividades_app.model.Entity.TipoActividad;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.model.dto.Contrato.ContratoRequestDTO;
import com.example.actividades_app.model.dto.Contrato.ContratoResponseDTO;
import com.example.actividades_app.repository.ContratoRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.ContratoService;
import com.example.actividades_app.repository.TipoActividadRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class ContratoServiceImpl implements ContratoService {

    private final ContratoRepository contratoRepository;
    private final UsuarioRepository usuarioRepository;
    private final TipoActividadRepository tipoActividadRepository;

    // =========================
    // CREAR
    // =========================
    @Override
    public ContratoResponseDTO crear(ContratoRequestDTO dto) {

        Usuario usuario = usuarioRepository.findById(dto.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TipoActividad tipoActividad = tipoActividadRepository
                .findById(dto.getTipoActividadId())
                .orElseThrow(() -> new RuntimeException("Tipo actividad no encontrada"));

                if (contratoRepository.existsByUsuarioIdAndTipoActividadIdAndEstado(
        usuario.getId(),
        tipoActividad.getId(),
        Estado.ACTIVO)) {

    throw new RuntimeException("El usuario ya tiene un contrato activo para esta actividad");
}
        if (tipoActividad.getTipoPlanilla() == TipoPlanilla.ADMINISTRATIVO
                && dto.getTipoPago() == TipoPago.PAGO_HORA) {

            throw new RuntimeException("Administrativo no puede tener pago por hora");
        }

        Contrato contrato = Contrato.builder()
                .tipoPago(dto.getTipoPago())
                .montoBase(dto.getMontoBase())
                .horasJornada(dto.getHorasJornada())
                .diasLaboralesMes(dto.getDiasLaboralesMes())
                .estado(dto.getEstado())
                .usuario(usuario)
                .tipoActividad(tipoActividad)
                .build();

        contratoRepository.save(contrato);

        return mapToDTO(contrato);
    }

    // =========================
    // ACTUALIZAR
    // =========================
    @Override
    public ContratoResponseDTO actualizar(Long id, ContratoRequestDTO dto) {

        Contrato contrato = contratoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));

        TipoActividad tipoActividad = tipoActividadRepository
                .findById(dto.getTipoActividadId())
                .orElseThrow(() -> new RuntimeException("Tipo actividad no encontrada"));

        contrato.setTipoPago(dto.getTipoPago());
        contrato.setMontoBase(dto.getMontoBase());
        contrato.setHorasJornada(dto.getHorasJornada());
        contrato.setDiasLaboralesMes(dto.getDiasLaboralesMes());
        contrato.setEstado(dto.getEstado());
        contrato.setTipoActividad(tipoActividad);

        contratoRepository.save(contrato);

        return mapToDTO(contrato);
    }

    // =========================
    // ELIMINAR
    // =========================
    @Override
    public void eliminar(Long id) {
        contratoRepository.deleteById(id);
    }

    // =========================
    // BUSCAR
    // =========================
    @Override
    public ContratoResponseDTO buscarPorId(Long id) {

        return contratoRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }

    // =========================
    // LISTAR
    // =========================
    @Override
    public List<ContratoResponseDTO> listar() {

        return contratoRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =========================
    // BUSCAR POR USUARIO
    // =========================
    @Override
    public ContratoResponseDTO buscarPorUsuario(Long usuarioId) {

        return contratoRepository.findByUsuarioId(usuarioId)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Contrato no encontrado"));
    }

    // =========================
    // MAPPER
    // =========================
    private ContratoResponseDTO mapToDTO(Contrato contrato) {

        return ContratoResponseDTO.builder()
                .id(contrato.getId())
                .tipoPago(contrato.getTipoPago())
                .montoBase(contrato.getMontoBase())
                .horasJornada(contrato.getHorasJornada())
                .diasLaboralesMes(contrato.getDiasLaboralesMes())
                .usuarioId(contrato.getUsuario().getId())
                .estado(contrato.getEstado())
                .tipoActividadId(
                        contrato.getTipoActividad() != null
                                ? contrato.getTipoActividad().getId()
                                : null)
                .build();
    }
}