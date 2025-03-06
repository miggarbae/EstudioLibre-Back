package com.proyecto.spring.controladores;

import com.proyecto.spring.modelos.*;
import com.proyecto.spring.servicios.*;
import com.proyecto.spring.dto.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    private final ComentarioService comentarioService;
    private final UsuarioService usuarioService;

    public ComentarioController(ComentarioService comentarioService, UsuarioService usuarioService) {
        this.comentarioService = comentarioService;
        this.usuarioService = usuarioService;
    }

   // 📌 Obtener comentarios de un archivo
   @GetMapping("/{archivoId}")
   public ResponseEntity<List<ComentarioDTO>> obtenerComentariosDeArchivo(@PathVariable Long archivoId) {
       return ResponseEntity.ok(comentarioService.obtenerComentariosDeArchivo(archivoId));
   }

   // 📌 Agregar un comentario a un archivo
   @PostMapping("/{archivoId}")
   public ResponseEntity<ComentarioDTO> agregarComentario(@PathVariable Long archivoId,
                                                          @RequestParam String texto,
                                                          @RequestParam int valoracion) {
       String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
       Usuario usuario = usuarioService.findByUsername(username)
               .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

       ComentarioDTO nuevoComentario = comentarioService.agregarComentario(archivoId, usuario, texto, valoracion);
       return ResponseEntity.ok(nuevoComentario);
   }

    // 📌 Editar comentario (solo el autor puede hacerlo)
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

    // 📌 Eliminar comentario (solo el autor puede hacerlo)
    @DeleteMapping("/{comentarioId}")
    public ResponseEntity<String> eliminarComentario(@PathVariable Long comentarioId) {
        String username = ((User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        Usuario usuario = usuarioService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        comentarioService.eliminarComentario(comentarioId, usuario);
        return ResponseEntity.ok("Comentario eliminado correctamente.");
    }
}
