package com.proyecto.spring.seguridad;

import com.proyecto.spring.servicios.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(CustomUserDetailsService userDetailsService, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(List.of(authProvider));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();
                // config.setAllowedOrigins(List.of("http://localhost:4200")); // Permitir Angular
                config.setAllowedOrigins(List.of("*"));
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Permitir todos los métodos
                config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Permitir headers necesarios
                config.setExposedHeaders(List.of("Authorization")); // Exponer el header Authorization
                config.setAllowCredentials(true); // Permitir credenciales
                return config;
            }))
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/perfiles/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/usuarios/*/imagen").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/*/imagen").authenticated()

                .requestMatchers(HttpMethod.GET, "/api/usuarios/*/imagen").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/usuarios/*/imagen").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/usuarios/perfil").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/usuarios/actualizar").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/api/usuarios/eliminar").authenticated()

                .requestMatchers(HttpMethod.GET, "/api/archivos/*").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/archivos/descargar/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/api/comentarios/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/api/archivos/editar/**").authenticated()
                .requestMatchers(HttpMethod.GET, "/api/comentarios/**").permitAll()

                .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                .requestMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/reportes").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/reportes").authenticated()

                .requestMatchers("/api/**").authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
