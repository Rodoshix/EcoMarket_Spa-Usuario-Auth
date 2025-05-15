package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.dto.LoginRequestDTO;
import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.security.JwtUtil;
import com.ecomarket.usuariosauth.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

@Autowired
private JwtUtil jwtUtil;

@Autowired
private UsuarioService usuarioService;

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequest) {
    Optional<Usuario> usuarioOpt = usuarioService.buscarPorEmail(loginRequest.getEmail());

    if (usuarioOpt.isEmpty()) {
        return ResponseEntity.status(401).body("Usuario no encontrado");
    }

    Usuario usuario = usuarioOpt.get();

    if (!usuario.getPassword().equals(loginRequest.getPassword())) {
        return ResponseEntity.status(401).body("Contraseña incorrecta");
    }

    String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

    return ResponseEntity.ok().body("{\"token\": \"" + token + "\"}");
}

}
