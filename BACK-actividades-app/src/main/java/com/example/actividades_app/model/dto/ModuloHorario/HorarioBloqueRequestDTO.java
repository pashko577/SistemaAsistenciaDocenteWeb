package com.example.actividades_app.model.dto.ModuloHorario;

import java.time.LocalTime;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HorarioBloqueRequestDTO {
    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;  // Sin @JsonFormat
    
    @NotNull(message = "La hora de fin es obligatoria")
    private LocalTime horaFin;      // Sin @JsonFormat
    
    @NotNull(message = "El orden del bloque es obligatorio")
    private Integer ordenBloque;
}