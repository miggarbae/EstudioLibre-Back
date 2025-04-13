package com.proyecto.spring.configuraciones;

import com.proyecto.spring.modelos.Rol;
import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.servicios.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataLoader {

    @Bean
    public CommandLineRunner cargarUsuarios(UsuarioService usuarioService) {
        return args -> {
            if (usuarioService.findByUsername("admin").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsername("admin");
                usuario.setPassword("admin"); // Se encripta automáticamente en UsuarioService
                usuario.setRol(Rol.ADMIN);
                usuario.setEmail("admin@prueba.com");
                usuarioService.save(usuario);
                System.out.println("Usuario de prueba creado: admin / admin");
            }
        };
    }
}