package com.example.actividades_app.service;


import com.example.actividades_app.model.Entity.Usuario;

import java.util.List;

public interface UsuarioService {

    Usuario registrar(Usuario usuario);
    List<Usuario> obtenerTodosLosUsuarios();
}
