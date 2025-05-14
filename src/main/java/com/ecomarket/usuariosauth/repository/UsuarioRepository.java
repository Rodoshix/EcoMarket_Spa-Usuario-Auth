package com.ecomarket.usuariosauth.repository;

import com.ecomarket.usuariosauth.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {   //findAll(); findById(Long id); save(Usuario u); deleteById(Long id)

    // Buscar usuario por email (útil para login)
    Optional<Usuario> findByEmail(String email);

    // Verificar si un email ya existe (para validación al registrar)
    boolean existsByEmail(String email);
}
