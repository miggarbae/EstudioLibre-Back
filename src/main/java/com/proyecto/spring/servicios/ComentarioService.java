package com.proyecto.spring.servicios;

import com.proyecto.spring.dto.ComentarioDTO;
import com.proyecto.spring.modelos.*;
import com.proyecto.spring.repositorios.ComentarioRepository;
import com.proyecto.spring.repositorios.ArchivoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final ArchivoRepository archivoRepository;

    public ComentarioService(ComentarioRepository comentarioRepository, ArchivoRepository archivoRepository) {
        this.comentarioRepository = comentarioRepository;
        this.archivoRepository = archivoRepository;
    }

    // Obtener comentarios de un archivo (DEVUELVE DTOs)
    public List<ComentarioDTO> obtenerComentariosDeArchivo(Long archivoId) {
        return comentarioRepository.findByArchivoId(archivoId)
                .stream()
                .map(c -> new ComentarioDTO(
                        c.getId(),
                        c.getUsuario().getUsername(),
                        c.getUsuario().getRutaImagenPerfil(),  // Nueva línea
                        c.getArchivo().getId(),
                        c.getTexto(),
                        c.getValoracion(),
                        c.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }

    // Agregar un comentario a un archivo
    public ComentarioDTO agregarComentario(Long archivoId, Usuario usuario, String texto, int valoracion) {
        if (valoracion < 1 || valoracion > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }

        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado"));

        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setArchivo(archivo);
        comentario.setTexto(texto);
        comentario.setValoracion(valoracion);
        comentario.setFechaCreacion(LocalDateTime.now());

        comentarioRepository.save(comentario);

        return new ComentarioDTO(
                comentario.getId(),
                usuario.getUsername(),
                usuario.getRutaImagenPerfil(),  // Nueva línea
                archivo.getId(),
                comentario.getTexto(),
                comentario.getValoracion(),
                comentario.getFechaCreacion()
        );
    }

    public Comentario editarComentario(Long comentarioId, Usuario usuario, String nuevoTexto, int nuevaValoracion) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        if (!comentario.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No tienes permiso para editar este comentario.");
        }

        comentario.setTexto(nuevoTexto);
        comentario.setValoracion(nuevaValoracion);
        return comentarioRepository.save(comentario);
    }

    public void eliminarComentario(Long comentarioId, Usuario usuario) {
        Comentario comentario = comentarioRepository.findById(comentarioId)
                .orElseThrow(() -> new IllegalArgumentException("Comentario no encontrado"));

        if (!comentario.getUsuario().getId().equals(usuario.getId())) {
            throw new IllegalStateException("No tienes permiso para eliminar este comentario.");
        }

        comentarioRepository.delete(comentario);
    }

    public int obtenerValoracionUsuario(Long archivoId, Usuario usuario) {
        return comentarioRepository.findByArchivoIdAndUsuarioId(archivoId, usuario.getId())
                .map(Comentario::getValoracion)
                .orElse(0);
    }

    public double obtenerValoracionMedia(Long archivoId) {
        List<Comentario> comentarios = comentarioRepository.findByArchivoId(archivoId);
        if (comentarios.isEmpty()) return 0;

        double suma = comentarios.stream()
                .mapToInt(Comentario::getValoracion)
                .sum();

        return (double) suma / comentarios.size();
    }
}