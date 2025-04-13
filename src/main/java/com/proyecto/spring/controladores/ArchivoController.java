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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
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
    public ResponseEntity<Map<String, String>> subirArchivo(
            @RequestParam("archivo") MultipartFile archivo,
            @RequestParam("descripcion") String descripcion,
            @RequestParam("asignatura") String asignatura,
            @RequestParam("nivelEstudio") String nivelEstudio) {
        
        try {
            // Obtener el usuario autenticado
            String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
            Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            // Guardar el archivo con la descripción, asignatura y nivel de estudios
            Archivo archivoGuardado = archivoService.guardarArchivo(archivo, usuario, descripcion, asignatura, nivelEstudio);

            // Devolver respuesta en JSON
            Map<String, String> response = new HashMap<>();
            response.put("message", "Archivo subido exitosamente");
            response.put("archivoId", archivoGuardado.getId().toString());

            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error al subir el archivo."));
        }
    }

    @GetMapping("/descarga/{id}")
    public ResponseEntity<?> descargarArchivo(@PathVariable Long id) {
        Optional<Archivo> archivoOpt = archivoService.obtenerArchivo(id);
    
        if (archivoOpt.isPresent()) {
            Archivo archivo = archivoOpt.get();
    
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + archivo.getNombre() + "\"")
                    .header(HttpHeaders.CONTENT_TYPE, archivo.getTipo()) // Mantener el tipo original
                    .body(archivo.getDatos()); // Devolver los datos sin modificar
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

    //Buscar archivos
    @PostMapping("/buscar")
    public ResponseEntity<List<Archivo>> buscarArchivos(@RequestBody ArchivoBusquedaDTO criterios) {
        System.out.println("🔍 Búsqueda recibida: " + criterios.getNombre()); 
    
        List<Archivo> resultados = archivoService.buscarArchivos(criterios.getNombre());
        System.out.println("📂 Archivos encontrados: " + resultados.size());
    
        return ResponseEntity.ok(resultados);
    }

    // Obtener todos los archivos
    @GetMapping("/todos")
    public ResponseEntity<List<Archivo>> obtenerArchivosVisibles() {
        return ResponseEntity.ok(archivoService.obtenerArchivosVisibles());
    }

    // Ruta para actualizar asignatura y nivel de un archivo
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarArchivo(@PathVariable Long id, @RequestBody Archivo archivoActualizado) {
        Optional<Archivo> archivoOpt = archivoService.obtenerArchivo(id);

        if (archivoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("mensaje", "Archivo no encontrado."));
        }

        Archivo archivo = archivoOpt.get();
        archivo.setAsignatura(archivoActualizado.getAsignatura());
        archivo.setNivelEstudio(archivoActualizado.getNivelEstudio());
        archivoService.guardarArchivoEditado(archivo);

        // Retornar un objeto JSON con un mensaje de éxito
        return ResponseEntity.ok(Map.of("mensaje", "Archivo actualizado correctamente."));
    }

    // Ruta para obtener un archivo por ID
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerArchivo(@PathVariable Long id) {
        Optional<Archivo> archivoOpt = archivoService.obtenerArchivo(id);

        if (archivoOpt.isPresent()) {
            return ResponseEntity.ok(archivoOpt.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado.");
        }
    }

    // Ruta para eliminar un archivo por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarArchivo(@PathVariable Long id) {
        Optional<Archivo> archivoOpt = archivoService.obtenerArchivo(id);

        if (archivoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado.");
        }

        archivoService.eliminarArchivoPorId(id);
        return ResponseEntity.ok(Map.of("mensaje", "Archivo eliminado correctamente."));
    }
}