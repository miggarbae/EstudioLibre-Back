package com.proyecto.spring.servicios;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.repositorios.usuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServicio {
    @Autowired
    private usuarioRepository usuarioRepositorio;

    public List<usuario> listarUsuarios() {
        return usuarioRepositorio.findAll();
    }

    public Optional<usuario> obtenerUsuarioPorId(Long id) {
        return usuarioRepositorio.findById(id);
    }

    public usuario guardarUsuario(usuario usuario) {
        return usuarioRepositorio.save(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepositorio.deleteById(id);
    }
}
