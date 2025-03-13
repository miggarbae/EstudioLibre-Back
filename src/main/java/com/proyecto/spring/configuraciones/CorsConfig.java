// package com.proyecto.spring.configuraciones;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.cors.CorsConfiguration;
// import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
// import org.springframework.web.filter.CorsFilter;

// import java.util.List;

// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsFilter corsFilter() {
//         CorsConfiguration config = new CorsConfiguration();
//         config.setAllowedOrigins(List.of("http://localhost:4200")); // Permitir Angular
//         config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS")); // Métodos permitidos
//         config.setAllowedHeaders(List.of("Authorization", "Content-Type")); // Headers permitidos
//         config.setExposedHeaders(List.of("Authorization")); // Exponer Authorization
//         config.setAllowCredentials(true); // Permitir credenciales

//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         source.registerCorsConfiguration("/**", config);

//         return new CorsFilter(source);
//     }
// }
