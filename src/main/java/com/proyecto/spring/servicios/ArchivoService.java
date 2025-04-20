package com.proyecto.spring.servicios;

import com.proyecto.spring.modelos.Archivo;
import com.proyecto.spring.modelos.Usuario;
import com.proyecto.spring.repositorios.ArchivoRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
// import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class ArchivoService {
    
    private final ArchivoRepository archivoRepository;

    // Inyección del repositorio en el constructor
    public ArchivoService(ArchivoRepository archivoRepository) {
        this.archivoRepository = archivoRepository;
    }

    private static final List<String> TIPOS_PERMITIDOS = List.of(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain",
        "application/rtf",
        "application/zip",
        "application/x-zip-compressed"
    );
    
    public Archivo guardarArchivo(MultipartFile archivo, Usuario usuario, String descripcion, String asignatura, String nivelEstudio) throws IOException {
        String tipo = archivo.getContentType();
        
        if (!TIPOS_PERMITIDOS.contains(tipo)) {
            throw new IllegalArgumentException("Solo se permiten archivos PDF, Word, TXT y RTF.");
        }
    
        Archivo nuevoArchivo = new Archivo();
        nuevoArchivo.setNombre(archivo.getOriginalFilename());
        nuevoArchivo.setTipo(tipo);
        nuevoArchivo.setDatos(archivo.getBytes());
        nuevoArchivo.setUsuario(usuario);
        nuevoArchivo.setDescripcion(descripcion);
        nuevoArchivo.setAsignatura(asignatura);
        nuevoArchivo.setNivelEstudio(nivelEstudio);
    
        return archivoRepository.save(nuevoArchivo);
    }    

    public Optional<Archivo> obtenerArchivo(Long id) {
        return archivoRepository.findById(id);
    }

    public List<Archivo> obtenerArchivosPorUsuario(Long usuarioId) {
        return archivoRepository.findByUsuarioId(usuarioId);
    }

    public List<Archivo> buscarArchivos(String termino) {
        return archivoRepository.buscarArchivos(termino);
    }

    public List<Archivo> obtenerTodosLosArchivos() {
        return archivoRepository.findAll();
    }

    public Archivo guardarArchivoEditado(Archivo archivo) {
        return archivoRepository.save(archivo);
    }

    public List<Archivo> obtenerArchivosVisibles() {
        return archivoRepository.obtenerArchivosVisibles();
    }

    public void eliminarArchivoPorId(Long id) {
        archivoRepository.deleteById(id);
    }    
}
