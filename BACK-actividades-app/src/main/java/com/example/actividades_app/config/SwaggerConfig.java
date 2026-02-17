package com.example.actividades_app.config;

import org.springframework.http.HttpHeaders;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        title = "Proyecto Asistencia",
        description = "Proyecto Asistencia",
        termsOfService = "0.0.0"
    ),
    servers = {
        @Server(
            description = "BACK Server",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "FRONT Server",
            url = "http://localhost:4200"
        )
    },
    security = @SecurityRequirement(
        name = "Security Token"
    )
)
@SecurityScheme(
    name = "Security Token",
    description = "Acceso de Token BEARER",
    type = SecuritySchemeType.HTTP,
    paramName = HttpHeaders.AUTHORIZATION,
    in = SecuritySchemeIn.HEADER,
    scheme = "bearer",
    bearerFormat = "JWT"
)
public class SwaggerConfig {}
