package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.*;
import com.proyecto.spring.repositorios.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    private final UsuarioRepository usuarioRepository;
    private final ArchivoRepository archivoRepository;
    private final ReporteRepository reporteRepository;

    public AdminController(UsuarioRepository usuarioRepository, ArchivoRepository archivoRepository, ReporteRepository reporteRepository) {
        this.usuarioRepository = usuarioRepository;
        this.archivoRepository = archivoRepository;
        this.reporteRepository = reporteRepository;
    }

    @GetMapping("/usuarios")
    public List<Map<String, Object>> obtenerUsuarios() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Map<String, Object>> resultado = new ArrayList<>();

        for (Usuario u : usuarios) {
            Map<String, Object> datos = new HashMap<>();
            datos.put("id", u.getId());
            datos.put("username", u.getUsername());
            datos.put("roles", u.getRol());
            datos.put("archivos", u.getArchivos());
            resultado.add(datos);
        }

        return resultado;
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> eliminarUsuario(@PathVariable Long id) {
        usuarioRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/usuarios/{id}/rol")
    public ResponseEntity<?> actualizarRol(@PathVariable Long id, @RequestBody Rol nuevoRol) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        usuario.setRol(nuevoRol);
        usuarioRepository.save(usuario);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/archivos/{id}")
    public ResponseEntity<?> eliminarArchivo(@PathVariable Long id) {
        archivoRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/archivos/{id}/visibilidad")
    public ResponseEntity<?> cambiarVisibilidad(@PathVariable Long id, @RequestParam boolean visible) {
        Archivo archivo = archivoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Archivo no encontrado"));
        archivo.setVisible(visible);
        archivoRepository.save(archivo);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/reportes/{id}")
    public ResponseEntity<?> eliminarReporte(@PathVariable Long id) {
        reporteRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/archivos/busqueda")
    public ResponseEntity<List<Archivo>> buscarArchivos(@RequestParam String termino) {
        return ResponseEntity.ok(archivoRepository.buscarPorNombreOUsuario(termino));
    }
}