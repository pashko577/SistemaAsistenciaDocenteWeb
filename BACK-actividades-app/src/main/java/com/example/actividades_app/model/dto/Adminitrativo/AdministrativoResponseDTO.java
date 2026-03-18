package com.example.actividades_app.model.dto.Adminitrativo;

import com.example.actividades_app.enums.Estado;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdministrativoResponseDTO {

    private Long id;
    private Long usuarioId;

    // Agregamos estos dos para que Angular pueda hacer: {{ admin.nombres }}
    private String nombres;
    private String apellidos;

    private String nombreUsuario; // opcional si quieres mostrarlo
    private Long cargoAdministrativoId;
    private String nombreCargo;
    private Estado estado;
    private String dni;
    private String email;
    private String celular;
    private String direccion;

    private Long sedeId;
    private String nombreSede;

}
