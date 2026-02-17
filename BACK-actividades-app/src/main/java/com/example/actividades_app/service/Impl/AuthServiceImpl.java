package com.example.actividades_app.service.Impl;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.example.actividades_app.config.JwtUtils;
import com.example.actividades_app.dto.AuthResponseDTO;
import com.example.actividades_app.dto.LoginRequestDTO;
import com.example.actividades_app.dto.RegisterRequestDTO;
import com.example.actividades_app.model.Entity.Rol;
import com.example.actividades_app.model.Entity.Usuario;
import com.example.actividades_app.repository.RolRepository;
import com.example.actividades_app.repository.UsuarioRepository;
import com.example.actividades_app.service.AuthService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public void register(RegisterRequestDTO request) {
        if(usuarioRepository.existsByUsername(request.getUsername())){
            throw new RuntimeException("USERNAME_ALREADY_EXISTS");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("EMAIL_ALREADY_EXISTS");
        }

        // Roles deben existir previamente en la base de datos
        Set<Rol> roles = request.getRoles().stream()
               .map(roleName -> rolRepository.findByName(roleName)
                       .orElseThrow(() -> new RuntimeException("ROLE_NOT_FOUND: " + roleName)))
                       .collect(Collectors.toSet());
        
        Usuario usuario = Usuario.builder()
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(roles)
                .build();
        usuarioRepository.save(usuario);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO request){

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        String username = authentication.getName();

        List<String> roles = authentication.getAuthorities()
            .stream()
            .map(grantedAuthority -> grantedAuthority.getAuthority())
            .toList();
        String token = jwtUtils.generateAccessToken(username, roles);

        return new AuthResponseDTO(token, username, roles);


    }

}
