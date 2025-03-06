package com.proyecto.spring.repositorios;

import com.proyecto.spring.modelos.Archivo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ArchivoRepository extends JpaRepository<Archivo, Long> {
    List<Archivo> findByUsuarioId(Long usuarioId);
    
    // 🔍 Búsqueda dinámica por nombre, asignatura y nivel de estudio
    @Query("""
        SELECT a FROM Archivo a
        WHERE (:nombre IS NULL OR LOWER(TRANSLATE(a.nombre, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE LOWER(TRANSLATE(CONCAT('%', :nombre, '%'), 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')))
        AND (:asignatura IS NULL OR LOWER(TRANSLATE(a.asignatura, 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')) LIKE LOWER(TRANSLATE(CONCAT('%', :asignatura, '%'), 'ÁÉÍÓÚáéíóú', 'AEIOUaeiou')))
        AND (:nivelEstudio IS NULL OR LOWER(a.nivelEstudio) = LOWER(:nivelEstudio))
    """)
    List<Archivo> buscarArchivos(
        @Param("nombre") String nombre,
        @Param("asignatura") String asignatura,
        @Param("nivelEstudio") String nivelEstudio
    );
}
