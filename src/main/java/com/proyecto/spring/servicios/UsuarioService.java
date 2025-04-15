package com.proyecto.spring.servicios;

import com.proyecto.spring.modelos.*;
import com.proyecto.spring.repositorios.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<Usuario> findByUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario save(Usuario usuario) {
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.USER);
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
    }

    // Método para eliminar un usuario
    public void eliminarUsuario(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}
