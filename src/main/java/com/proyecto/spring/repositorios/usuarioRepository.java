package com.proyecto.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.proyecto.spring.modelo.usuario;

@Repository
public interface usuarioRepository extends JpaRepository<usuario, Long> {
    usuario findByEmail(String email);
}
