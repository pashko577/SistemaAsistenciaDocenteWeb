package com.example.actividades_app.service;

import java.util.List;

import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoRequestDTO;
import com.example.actividades_app.model.dto.Adminitrativo.AdministrativoResponseDTO;

public interface AdministrativoService {
    AdministrativoResponseDTO registrarAdministrativo(AdministrativoRequestDTO request);

    AdministrativoResponseDTO actualizarAdministrativo(Long id, AdministrativoRequestDTO request);

    void eliminarAdministrativo(Long id);

    List<AdministrativoResponseDTO> listarAdministrativos();

    List<AdministrativoResponseDTO> listarSoloConContrato();
}
