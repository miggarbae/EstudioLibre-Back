package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.*;
import com.proyecto.spring.seguridad.*;
import com.proyecto.spring.servicios.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UsuarioService usuarioService;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Usuario usuario) {
        System.out.println("Intentando autenticar usuario: " + usuario.getUsername());

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getUsername(), usuario.getPassword())
            );
        } catch (Exception e) {
            System.out.println("Error de autenticación: " + e.getMessage());
            return ResponseEntity.status(403).body("Error de autenticación: " + e.getMessage());
        }

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        String role = user.getAuthorities().stream().findFirst().get().getAuthority(); // obtiene el primer rol

        // Generar token con username y role
        String token = jwtUtil.generateToken(username, role);

        System.out.println("Autenticación exitosa, token generado con rol: " + role);
        return ResponseEntity.ok(Map.of("token", token));
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        if (usuarioService.findByUsername(usuario.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("mensaje", "El usuario ya existe"));
        }

        usuario.setRol(Rol.USER); // Asegurar que tenga al menos un rol
        Usuario savedUser = usuarioService.save(usuario);
        return ResponseEntity.ok(Map.of("mensaje", "Usuario registrado exitosamente"));
    }
}