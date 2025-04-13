package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.*;
import com.proyecto.spring.servicios.*;
import com.proyecto.spring.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;

    public ComentarioController(ComentarioService comentarioService, UsuarioService usuarioService) {
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

   // Obtener comentarios de un archivo
   @GetMapping("/{archivoId}")
   public ResponseEntity<List<ComentarioDTO>> obtenerComentariosDeArchivo(@PathVariable Long archivoId) {
       return ResponseEntity.ok(comentarioService.obtenerComentariosDeArchivo(archivoId));
   }

   // Agregar un comentario a un archivo
   @PostMapping("/{archivoId}")
    public ResponseEntity<ComentarioDTO> agregarComentario(
        @PathVariable Long archivoId,
        @RequestBody ComentarioDTO comentarioDTO
    ) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        ComentarioDTO nuevoComentario = comentarioService.agregarComentario(
                archivoId, usuario, comentarioDTO.getTexto(), comentarioDTO.getValoracion()
        );
        
        return ResponseEntity.ok(nuevoComentario);
    }

    @PostMapping("/valorar/{archivoId}")
    public ResponseEntity<Void> valorarArchivo(
            @PathVariable Long archivoId,
            @RequestParam int valoracion
    ) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        comentarioService.valorarArchivo(archivoId, usuario, valoracion);
        return ResponseEntity.ok().build();
    }

    // Editar comentario (solo el autor puede hacerlo)
    @PutMapping("/{comentarioId}")
    public ResponseEntity<Comentario> editarComentario(@PathVariable Long comentarioId,
                                                       @RequestParam String nuevoTexto,
                                                       @RequestParam int nuevaValoracion) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Comentario comentarioEditado = comentarioService.editarComentario(comentarioId, usuario, nuevoTexto, nuevaValoracion);
        return ResponseEntity.ok(comentarioEditado);
    }

    // Eliminar comentario (solo el autor puede hacerlo)
    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<Map<String, String>> eliminarComentario(@PathVariable Long comentarioId) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        comentarioService.eliminarComentario(comentarioId, usuario);

        // Enviar una respuesta en formato JSON
        Map<String, String> response = new HashMap<>();
        response.put("message", "Comentario eliminado correctamente.");
        
        return ResponseEntity.ok(response);
    }

    // Obtener la valoración del usuario actual para un archivo
    @GetMapping("/valoracion/{archivoId}")
    public ResponseEntity<Integer> obtenerValoracionUsuario(@PathVariable Long archivoId) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        int valoracion = comentarioService.obtenerValoracionUsuario(archivoId, usuario);
        return ResponseEntity.ok(valoracion);
    }

    // Obtener la valoración media de un archivo
    @GetMapping("/valoracion-media/{archivoId}")
    public ResponseEntity<Double> obtenerValoracionMedia(@PathVariable Long archivoId) {
        double media = comentarioService.obtenerValoracionMedia(archivoId);
        return ResponseEntity.ok(media);
    }
}
