package com.example.actividades_app.service.Impl;

import com.example.actividades_app.enums.Estado;
import com.example.actividades_app.model.Entity.Administrativo;
import com.example.actividades_app.repository.AdministrativoRepository;
import com.example.actividades_app.service.AdministrativoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdministrativoServiceImpl implements AdministrativoService {

        private final AdministrativoRepository repository;

        @Override
        public Administrativo crear(Administrativo administrativo) {

                administrativo.setEstado(Estado.ACTIVO);

                return repository.save(administrativo);
        }

        @Override
        public Administrativo actualizar(Long id, Administrativo administrativo) {

                Administrativo existente = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Administrativo no encontrado"));

                existente.setCargoAdministrativo(administrativo.getCargoAdministrativo());
                existente.setEstado(administrativo.getEstado());

                return repository.save(existente);
        }

        @Override
        public Optional<Administrativo> obtenerPorId(Long id) {
                return repository.findById(id);
        }

        @Override
        public List<Administrativo> listarTodos() {
                return repository.findAll();
        }

        @Override
        public List<Administrativo> listarActivos() {
                return repository.findByEstado(Estado.ACTIVO);
        }

        public List<Administrativo> listarInactivos() {
                return repository.findByEstado(Estado.INACTIVO);
        }

        public List<Administrativo> listarSuspendidos() {
                return repository.findByEstado(Estado.SUSPENDIDO);
        }

        @Override
        public void eliminar(Long id) {
                repository.deleteById(id);
        }
}
