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
            if (usuarioService.findByUsername("usuario1").isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setUsername("usuario1");
                usuario.setPassword("1234"); // Se encripta automáticamente en UsuarioService
                usuario.setRol(Rol.ADMIN);
                usuarioService.save(usuario);
                System.out.println("Usuario de prueba creado: usuario1 / 1234");
            }
        };
    }
}
