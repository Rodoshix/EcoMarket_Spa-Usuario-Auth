package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.services.UsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    // Crear nuevo usuario (registro)
    @Operation(summary = "Registrar nuevo usuario")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario creado exitosamente"),
        @ApiResponse(responseCode = "409", description = "Correo electrónico ya existe")
    })
    @PostMapping("/registro")
    public ResponseEntity<?> registrarUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.emailExiste(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Ya existe un usuario con ese correo electrónico.");
    }
        Usuario nuevoUsuario = usuarioService.registrarUsuario(usuario);
        return ResponseEntity.ok(nuevoUsuario);
    }

    // Obtener usuario por email (por ejemplo, para login)
    @Operation(summary = "Buscar usuario por email")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuario encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario no encontrado")
    })
    @GetMapping("/buscar")
    public ResponseEntity<Usuario> buscarPorEmail(@RequestParam String email) {
        Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(email);
        return usuarioOpt.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Eliminar usuario (opcional)
    @Operation(summary = "Eliminar usuario por ID")
    @ApiResponse(responseCode = "204", description = "Usuario eliminado correctamente")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Long id) {
        usuarioService.eliminarUsuario(id);
        return ResponseEntity.noContent().build();
    }

    // Obtener todos los usuarios (opcional)
    @Operation(summary = "Listar todos los usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios obtenida correctamente")
    @GetMapping
    public ResponseEntity<Iterable<Usuario>> obtenerTodos() {
        return ResponseEntity.ok(usuarioService.obtenerTodos());
    }
}
