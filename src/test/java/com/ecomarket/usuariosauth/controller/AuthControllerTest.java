package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.dto.LoginRequestDTO;
import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.security.JwtFilter;
import com.ecomarket.usuariosauth.security.JwtUtil;
import com.ecomarket.usuariosauth.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_exitoso_deberiaRetornarToken() throws Exception {
        // Arrange
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("cliente@email.com");
        loginRequest.setPassword("clave123");

        Usuario usuario = new Usuario(1L, "Cliente", "cliente@email.com", "clave123", "CLIENTE");

        Mockito.when(usuarioService.buscarPorEmail("cliente@email.com"))
               .thenReturn(Optional.of(usuario));
        Mockito.when(jwtUtil.generarToken("cliente@email.com", "CLIENTE"))
               .thenReturn("jwt-token-fake");

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(content().json("{\"token\":\"jwt-token-fake\"}"));
    }

    @Test
    void login_usuarioNoExiste_deberiaRetornar401() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("desconocido@email.com");
        loginRequest.setPassword("clave123");

        Mockito.when(usuarioService.buscarPorEmail("desconocido@email.com"))
               .thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Usuario no encontrado"));
    }

    @Test
    void login_passwordIncorrecta_deberiaRetornar401() throws Exception {
        LoginRequestDTO loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("cliente@email.com");
        loginRequest.setPassword("claveMala");

        Usuario usuario = new Usuario(1L, "Cliente", "cliente@email.com", "claveBuena", "CLIENTE");

        Mockito.when(usuarioService.buscarPorEmail("cliente@email.com"))
               .thenReturn(Optional.of(usuario));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Contraseña incorrecta"));
    }
}