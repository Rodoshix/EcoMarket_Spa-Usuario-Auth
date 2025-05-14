package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Crear nuevo usuario (registro)
    @PostMapping("/registro")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.emailExiste(usuario.getEmail())) {
            return ResponseEntity.badRequest().build(); // Email ya registrado
        }
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Obtener usuario por email (por ejemplo, para login)
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        return usuarioOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar usuario (opcional)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los usuarios (opcional)
    @GetMapping
    public ResponseEntity<Iterable<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
}
