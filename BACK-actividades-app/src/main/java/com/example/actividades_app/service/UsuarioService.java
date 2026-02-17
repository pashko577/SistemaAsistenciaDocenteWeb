package com.example.actividades_app.service;

import java.util.Optional;

import com.example.actividades_app.model.Entity.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);
    Optional<Usuario> buscarPorUsername(String username);
    List<Usuario> obtenerTodosLosUsuarios();
}
