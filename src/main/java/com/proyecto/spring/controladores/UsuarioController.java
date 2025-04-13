package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.servicios.UsuarioService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.io.IOException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final PasswordEncoder passwordEncoder;
    private static final String DIRECTORIO_IMAGENES = "uploads/perfiles/";

    public UsuarioController(UsuarioService usuarioService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.passwordEncoder = passwordEncoder;
    }

    // Obtener perfil del usuario autenticado
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(userDetails.getUsername());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();
        usuario.setPassword(null); // Nunca enviar la contraseña
        return ResponseEntity.ok(usuario);
    }

    // Actualizar perfil
    @PutMapping("/actualizar")
    public ResponseEntity<?> actualizarPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false) String email,
                                              @RequestParam(required = false) String password) {

        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(userDetails.getUsername());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Usuario usuario = usuarioOpt.get();

        if (username != null && !username.equals(usuario.getUsername())) {
            usuario.setUsername(username);
        }

        if (email != null && !email.equals(usuario.getEmail())) {
            usuario.setEmail(email);
        }

        if (password != null && !password.isEmpty()) {
            usuario.setPassword(passwordEncoder.encode(password));
        }

        Usuario actualizado = usuarioService.saveDirect(usuario);
        actualizado.setPassword(null);

        return ResponseEntity.ok(actualizado);
    }

    // Eliminar cuenta del usuario autenticado (y sus archivos relacionados)
    @DeleteMapping("/eliminar")
    public ResponseEntity<?> eliminarCuenta(@AuthenticationPrincipal UserDetails userDetails) {
        Optional<Usuario> usuarioOpt = usuarioService.findByUsername(userDetails.getUsername());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        usuarioService.eliminarUsuario(usuarioOpt.get());
        return ResponseEntity.ok("Usuario eliminado correctamente.");
    }

    // Endpoint para subir la imagen de perfil
    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> subirImagen(@PathVariable Long id, @RequestParam("imagen") MultipartFile imagen) {
        try {
            Usuario usuario = usuarioService.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            String nombreArchivo = UUID.randomUUID().toString() + "_" + imagen.getOriginalFilename();
            Path rutaArchivo = Paths.get(DIRECTORIO_IMAGENES).resolve(nombreArchivo);
            Files.createDirectories(rutaArchivo.getParent());
            Files.copy(imagen.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
    
            usuario.setRutaImagenPerfil("/perfiles/" + nombreArchivo);
            usuarioService.save(usuario);
    
            // Devolver respuesta en formato JSON
            return ResponseEntity.ok().body(Map.of(
                "mensaje", "Imagen subida exitosamente",
                "ruta", rutaArchivo.toString()
            ));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Map.of("error", "Error al subir la imagen")
            );
        }
    }
}
