package com.example.actividades_app.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import com.example.actividades_app.config.JwtUtils;
import com.example.actividades_app.security.filters.JwtAuthenticationFilter;
import com.example.actividades_app.security.filters.JwtAutorizationFilter;
import com.example.actividades_app.service.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

        @Autowired
        JwtAutorizationFilter autorizationFilter;

        @Autowired
        JwtUtils jwtUtils;

        @Autowired
        UserDetailsServiceImpl userDetailsService;

        @Bean
        SecurityFilterChain securityFilterChain(
                        HttpSecurity http,
                        AuthenticationManager authenticationManager) throws Exception {

                JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
                jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
                jwtAuthenticationFilter.setFilterProcessesUrl("/api/auth/login");

                return http
                                .cors(cors -> {
                                })
                                .csrf(csrf -> csrf.disable())

                                .authorizeHttpRequests(auth -> auth
                                                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                                                .requestMatchers(
                                                                "/api/auth/register",
                                                                "/api/auth/login")
                                                .permitAll()

                                                .requestMatchers(
                                                                "/v3/api-docs/**",
                                                                "/swagger-ui/**",
                                                                "/swagger-ui.html")
                                                .permitAll()

                                                .anyRequest().authenticated())

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                // LOGIN FILTER
                                .addFilter(jwtAuthenticationFilter)

                                // TOKEN VALIDATION FILTER
                                .addFilterBefore(
                                                autorizationFilter,
                                                UsernamePasswordAuthenticationFilter.class)

                                .build();

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
                return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                                .userDetailsService(userDetailsService)
                                .passwordEncoder(passwordEncoder())
                                .and().build();
        }

}