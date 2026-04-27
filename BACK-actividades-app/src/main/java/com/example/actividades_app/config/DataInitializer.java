package com.example.actividades_app.config;

import com.example.actividades_app.model.Entity.Modulo;
import com.example.actividades_app.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ModuloRepository moduloRepository;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            // Rutas Globales: Sin prefijos /admin o /user. 
            // Esto permite que cualquier rol con permiso acceda a la misma URL.
            List<Modulo> modulosConfig = List.of(
                crearModulo("Dashboard", "Panel principal", "/dashboard"),
                
                // Gestión (Acceso según rol)
                crearModulo("Gestión Docente", "Mantenimiento de profesores", "/gestion-docente"),
                crearModulo("Gestión Administrativo", "Mantenimiento de personal", "/gestion-administrativo"),
                crearModulo("Gestión de Contratos", "Control de contratos", "/gestion-contratos"),
                crearModulo("Gestión Académica", "Configuración académica", "/gestion-academica"),
                crearModulo("Asignación Docente", "Asignación de clases", "/asignacion-docente"),

                // Asistencias
                crearModulo("Asistencia Docente", "Registro para profesores", "/asistencia-docente"),
                crearModulo("Asistencia Administrativo", "Registro para administrativos", "/asistencia-administrativo"),

                // Reportes y Tesorería
                crearModulo("Reporte Administrativo", "Generación de reportes", "/reporte-administrativo"),
                crearModulo("Generar Pagos", "Módulo de pagos", "/pagos"),

                // Configuración y Perfil
                crearModulo("Gestión de Permisos", "Administración de roles", "/permisos"),
                crearModulo("Mi Perfil", "Ajustes de usuario", "/perfil"),
                crearModulo("Configuración", "Ajustes del sistema", "/configuracion"),
                crearModulo("Notificaciones", "Alertas", "/notificaciones")
            );

            for (Modulo m : modulosConfig) {
                moduloRepository.findByNombre(m.getNombre())
                    .ifPresentOrElse(
                        existente -> {
                            if (!existente.getRuta().equals(m.getRuta())) {
                                existente.setRuta(m.getRuta());
                                moduloRepository.save(existente);
                                System.out.println("Ruta actualizada: " + m.getNombre());
                            }
                        },
                        () -> {
                            moduloRepository.save(m);
                            System.out.println("Módulo creado: " + m.getNombre());
                        }
                    );
            }

            // INICIALIZACIÓN DE TIPOS DE DEDUCCIÓN CON IDs ESPECÍFICOS PARA PAGOS
            String[] queriesDeduccion = {
                "INSERT IGNORE INTO tipo_deduccion (tipo_deduccionid, nombre) VALUES (1, 'Automática');",
                "INSERT IGNORE INTO tipo_deduccion (tipo_deduccionid, nombre) VALUES (2, 'ONP o AFP');",
                "INSERT IGNORE INTO tipo_deduccion (tipo_deduccionid, nombre) VALUES (3, 'ESSALUD');",
                "INSERT IGNORE INTO tipo_deduccion (tipo_deduccionid, nombre) VALUES (4, 'Descuento por Hijos');",
                "INSERT IGNORE INTO tipo_deduccion (tipo_deduccionid, nombre) VALUES (5, 'Otros Descuentos Administrativos');"
            };

            for (String sql : queriesDeduccion) {
                try {
                    jdbcTemplate.execute(sql);
                } catch (Exception e) {
                    System.out.println("Nota SQL Deducciones (ignorable si ya existen): " + e.getMessage());
                }
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