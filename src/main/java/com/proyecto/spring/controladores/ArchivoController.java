package com.proyecto.spring.controladores;

import com.proyecto.spring.dto.ArchivoBusquedaDTO;
import com.proyecto.spring.modelos.*;
import com.proyecto.spring.servicios.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/archivos")
public class ArchivoController {

    private final ArchivoService archivoService;
    private final UsuarioService usuarioService;

    public ArchivoController(ArchivoService archivoService, UsuarioService usuarioService) {
        this.archivoService = archivoService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/subir")
    public ResponseEntity<?> subirArchivo(@RequestParam("archivo") MultipartFile archivo) {
        try {
            String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Usuario usuario = usuarioService.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            Archivo archivoGuardado = archivoService.guardarArchivo(archivo, usuario);
            return ResponseEntity.ok("Archivo subido exitosamente con ID: " + archivoGuardado.getId());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al subir el archivo.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/descargar/{id}")
    public ResponseEntity<?> descargarArchivo(@PathVariable Long id) {
        Optional<Archivo> archivoOpt = archivoService.obtenerArchivo(id);

        if (archivoOpt.isPresent()) {
            Archivo archivo = archivoOpt.get();
            try {
                byte[] pdfData = archivoService.convertirArchivoAPDF(archivo);
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombre() + ".pdf\"")
                        .body(pdfData);
            } catch (IOException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al convertir el archivo a PDF.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado.");
        }
    }

    // Obtener archivos subidos por usuario
    @GetMapping("/mis-archivos")
    public ResponseEntity<List<Archivo>> obtenerMisArchivos() {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return ResponseEntity.ok(archivoService.obtenerArchivosPorUsuario(usuario.getId()));
    }

    @PostMapping("/buscar")
    public ResponseEntity<List<Archivo>> buscarArchivos(@RequestBody ArchivoBusquedaDTO criterios) {
        return ResponseEntity.ok(archivoService.buscarArchivos(criterios));
    }
}

