package com.proyecto.spring.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.spring.modelo.usuario;
public interface usuarioRepository extends JpaRepository<usuario, Long> {

}
