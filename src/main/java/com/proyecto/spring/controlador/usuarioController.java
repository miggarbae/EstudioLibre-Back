package com.proyecto.spring.controlador;

import com.proyecto.spring.modelo.*;
import com.proyecto.spring.servicios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:4200") //esto es para permitir que angular se comunique con este controlador
@RequestMapping("/usuarios")
public class usuarioController {
    @Autowired
    private UsuarioServicio usuarioServicio;

    @GetMapping
    public List<usuario> listarUsuarios() {
        return usuarioServicio.listarUsuarios();
    }

    @GetMapping("/{id}")
    public Optional<usuario> obtenerUsuario(@PathVariable Long id) {
        return usuarioServicio.obtenerUsuarioPorId(id);
    }

    @PostMapping
    public usuario crearUsuario(@RequestBody usuario usuario) {
        return usuarioServicio.guardarUsuario(usuario);
    }

    @DeleteMapping("/{id}")
    public void eliminarUsuario(@PathVariable Long id) {
        usuarioServicio.eliminarUsuario(id);
    }
}
