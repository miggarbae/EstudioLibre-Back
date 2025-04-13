package com.proyecto.spring.servicios;

import com.proyecto.spring.dto.ComentarioDTO;
import com.proyecto.spring.modelos.*;
import com.proyecto.spring.repositorios.ComentarioRepository;
import com.proyecto.spring.repositorios.ArchivoRepository;

import java.util.Optional;
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

    // Obtener comentarios de un archivo
    public List<ComentarioDTO> obtenerComentariosDeArchivo(Long archivoId) {
        return comentarioRepository.findByArchivoId(archivoId)
                .stream()
                .map(c -> new ComentarioDTO(
                        c.getId(),
                        c.getUsuario().getUsername(),
                        c.getUsuario().getRutaImagenPerfil(),
                        c.getArchivo().getId(),
                        c.getTexto(),
                        c.getValoracion() != null ? c.getValoracion() : 0,
                        c.getFechaCreacion()
                ))
                .collect(Collectors.toList());
    }

    // Agregar comentario y/o valoración
    public ComentarioDTO agregarComentario(Long archivoId, Usuario usuario, String texto, Integer valoracion) {
        if ((texto == null || texto.trim().isEmpty()) && (valoracion == null || valoracion < 1 || valoracion > 5)) {
            throw new IllegalArgumentException("Debes escribir un comentario o proporcionar una valoración válida.");
        }
    
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado"));
    
        // Si solo se envía valoración (sin comentario), actualiza o crea una entrada mínima
        if ((texto == null || texto.trim().isEmpty()) && valoracion != null) {
            // Si ya existe un comentario del usuario para ese archivo, actualiza la valoración
            return comentarioRepository.findByArchivoIdAndUsuarioId(archivoId, usuario.getId())
                    .map(comentarioExistente -> {
                        comentarioExistente.setValoracion(valoracion);
                        comentarioRepository.save(comentarioExistente);
                        return new ComentarioDTO(
                                comentarioExistente.getId(),
                                usuario.getUsername(),
                                usuario.getRutaImagenPerfil(),
                                archivo.getId(),
                                comentarioExistente.getTexto(),
                                comentarioExistente.getValoracion(),
                                comentarioExistente.getFechaCreacion()
                        );
                    })
                    .orElseGet(() -> {
                        // Crear un nuevo comentario vacío con valoración (sin texto)
                        Comentario nuevo = new Comentario();
                        nuevo.setUsuario(usuario);
                        nuevo.setArchivo(archivo);
                        nuevo.setTexto("");  // texto vacío intencional
                        nuevo.setValoracion(valoracion);
                        nuevo.setFechaCreacion(LocalDateTime.now());
                        comentarioRepository.save(nuevo);
    
                        return new ComentarioDTO(
                                nuevo.getId(),
                                usuario.getUsername(),
                                usuario.getRutaImagenPerfil(),
                                archivo.getId(),
                                nuevo.getTexto(),
                                nuevo.getValoracion(),
                                nuevo.getFechaCreacion()
                        );
                    });
        }
    
        // Comentario con o sin valoración válida
        Comentario comentario = new Comentario();
        comentario.setUsuario(usuario);
        comentario.setArchivo(archivo);
        comentario.setTexto(texto.trim());
        comentario.setValoracion((valoracion != null && valoracion >= 1 && valoracion <= 5) ? valoracion : 0);
        comentario.setFechaCreacion(LocalDateTime.now());
    
        comentarioRepository.save(comentario);
    
        return new ComentarioDTO(
                comentario.getId(),
                usuario.getUsername(),
                usuario.getRutaImagenPerfil(),
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
                .map(c -> c.getValoracion() != null ? c.getValoracion() : 0)
                .orElse(0);
    }

    public double obtenerValoracionMedia(Long archivoId) {
        List<Comentario> comentarios = comentarioRepository.findByArchivoId(archivoId);

        double suma = comentarios.stream()
                .filter(c -> c.getValoracion() != null)
                .mapToInt(Comentario::getValoracion)
                .sum();

        long total = comentarios.stream()
                .filter(c -> c.getValoracion() != null)
                .count();

        return total == 0 ? 0 : (double) suma / total;
    }

    public void valorarArchivo(Long archivoId, Usuario usuario, int valoracion) {
        if (valoracion < 1 || valoracion > 5) {
            throw new IllegalArgumentException("La valoración debe estar entre 1 y 5.");
        }
    
        Archivo archivo = archivoRepository.findById(archivoId)
                .orElseThrow(() -> new IllegalArgumentException("Archivo no encontrado"));
    
        // Si el usuario ya comentó, actualiza la valoración sin tocar el texto
        Optional<Comentario> existente = comentarioRepository.findByArchivoIdAndUsuarioId(archivoId, usuario.getId());
        if (existente.isPresent()) {
            Comentario comentario = existente.get();
            comentario.setValoracion(valoracion);
            comentarioRepository.save(comentario);
        } else {
            // Solo guarda la valoración sin texto
            Comentario comentario = new Comentario();
            comentario.setUsuario(usuario);
            comentario.setArchivo(archivo);
            comentario.setTexto(""); // <-- vacío, pero lo ignorarás en frontend
            comentario.setValoracion(valoracion);
            comentario.setFechaCreacion(LocalDateTime.now());
            comentarioRepository.save(comentario);
        }
    }    
}
