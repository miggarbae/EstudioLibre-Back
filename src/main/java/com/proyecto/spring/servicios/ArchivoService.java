package com.proyecto.spring.servicios;

import com.proyecto.spring.modelos.Archivo;
import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.repositorios.ArchivoRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ArchivoService {
    private final ArchivoRepository archivoRepository;

    public ArchivoService(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    public Archivo guardarArchivo(MultipartFile archivo, Usuario usuario) throws IOException {
        Archivo nuevoArchivo = new Archivo();
        nuevoArchivo.setNombre(archivo.getOriginalFilename());
        nuevoArchivo.setTipo(archivo.getContentType());
        nuevoArchivo.setDatos(archivo.getBytes());
        nuevoArchivo.setUsuario(usuario);
        return archivoRepository.save(nuevoArchivo);
    }

    public Optional<Archivo> obtenerArchivo(Long id) {
        return archivoRepository.findById(id);
    }

    public List<Archivo> obtenerArchivosPorUsuario(Long usuarioId) {
        return archivoRepository.findByUsuarioId(usuarioId);
    }
}
