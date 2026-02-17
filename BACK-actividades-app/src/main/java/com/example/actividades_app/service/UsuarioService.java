package com.example.actividades_app.service;

import com.example.actividades_app.model.Usuario;
import java.util.Optional;
import java.util.List;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);
    Optional<Usuario> buscarPorUsername(String username);
    List<Usuario> obtenerTodosLosUsuarios();
}
