package com.ecomarket.usuariosauth.services;


import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Guardar usuario (registro)
    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // Buscar usuario por email (para login)
    public Optional<Usuario> buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Validar si un email ya está registrado
    public boolean emailExiste(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    // Obtener todos los usuarios (opcional, por administración)
    public Iterable<Usuario> obtenerTodos() {
        return usuarioRepository.findAll();
    }

    // Eliminar usuario por ID (opcional)
    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    // Obtener usuario por ID
    public Usuario obtenerPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
}