package com.example.actividades_app.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.stream.Collectors;

import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String dni) throws UsernameNotFoundException {
        
        Usuario usuario = usuarioRepository.findByDni(dni)
            .orElseThrow(() -> new UsernameNotFoundException("No existe usuario con DNI: " + dni));
          
        Collection<? extends GrantedAuthority> authorities = usuario.getRoles()
            .stream()
            .map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getNombreRol())))
            .collect(Collectors.toSet()); 

        return new User(usuario.getPersona().getDni(), usuario.getPassword(),
            true, true, true, true, authorities);
    }
}