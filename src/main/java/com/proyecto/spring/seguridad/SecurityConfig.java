package com.proyecto.spring.seguridad;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.List;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF ya que usaremos JWT (evita ataques de sesión)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**", "/login", "/register").permitAll() // Endpoints públicos
                .requestMatchers("/admin/**").hasRole("ADMIN") // Endpoints solo para ADMIN
                .anyRequest().authenticated() // Todo lo demás requiere autenticación
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless para JWT
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class) // Filtro JWT antes del login
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Para H2

        return http.build();
    }

    @Bean //esto es una carga de Usuario en memoria para pruebas, en producción se debe usar una base de datos
    public UserDetailsService userDetailsService() {
        return username -> User.withUsername(username)
            .password(passwordEncoder().encode("password")) // En un sistema real, usar base de datos
            .roles("USER")
            .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Codifica contraseñas para seguridad
    }
}