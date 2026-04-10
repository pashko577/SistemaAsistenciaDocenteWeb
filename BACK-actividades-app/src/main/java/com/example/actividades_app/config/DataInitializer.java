package com.example.actividades_app.config;

import com.example.actividades_app.model.Entity.Modulo;
import com.example.actividades_app.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ModuloRepository moduloRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Definimos la lista exacta de tu sidebar de Angular
            List<Modulo> modulosConfig = List.of(
                // SECCIÓN: Principal
                crearModulo("Dashboard", "Panel principal de control", "/admin/dashboard"),

                // SECCIÓN: Administración
                crearModulo("Gestión Docente", "Mantenimiento de profesores", "/admin/gestionDocente"),
                crearModulo("Gestión Administrativo", "Mantenimiento de personal administrativo", "/admin/gestionAdministrativo"),
                crearModulo("Gestión de Contratos", "Visualización y control de contratos", "/admin/gestionContrato"),

                // SECCIÓN: Asistencias
                crearModulo("Asistencia Docente", "Registro de asistencia de profesores", "/admin/asistencia/docente"),
                crearModulo("Asistencia Administrativo", "Registro de asistencia administrativa", "/admin/asistencia-administrativo"),

                // SECCIÓN: Reportes y Pagos
                crearModulo("Reporte Administrativo", "Generación de reportes del sistema", "/admin/reporte-administrativo"),
                crearModulo("Generar Pagos", "Módulo de tesorería y pagos", "/admin/pagos"),

                // SECCIÓN: Configuración
                crearModulo("Gestión de Permisos", "Administración de accesos por rol", "/admin/permisos"),
                crearModulo("Mi Perfil", "Configuración de perfil de usuario", "/admin/perfil"),
                crearModulo("Configuración", "Ajustes generales del sistema", "/admin/configuracion"),
                crearModulo("Notificaciones", "Centro de alertas y mensajes", "/admin/notificaciones")
            );

            for (Modulo m : modulosConfig) {
                moduloRepository.findByNombre(m.getNombre())
                    .ifPresentOrElse(
                        existente -> {
                            // Si el nombre ya existe, verificamos si la ruta cambió para actualizarla
                            if (!existente.getRuta().equals(m.getRuta())) {
                                existente.setRuta(m.getRuta());
                                moduloRepository.save(existente);
                                System.out.println("Ruta actualizada para: " + m.getNombre());
                            }
                        },
                        () -> {
                            // Si no existe el nombre, creamos el registro nuevo
                            moduloRepository.save(m);
                            System.out.println("Módulo creado: " + m.getNombre());
                        }
                    );
            }
        };
    }

    private Modulo crearModulo(String nombre, String desc, String ruta) {
        return Modulo.builder()
                .nombre(nombre)
                .descripcion(desc)
                .ruta(ruta)
                .build();
    }
}