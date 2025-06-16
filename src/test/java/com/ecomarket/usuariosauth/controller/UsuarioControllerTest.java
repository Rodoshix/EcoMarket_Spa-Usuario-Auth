package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.security.JwtFilter;
import com.ecomarket.usuariosauth.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private JwtFilter jwtFilter;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registrarUsuario_exitoso() throws Exception {
        Usuario nuevoUsuario = new Usuario(null, "Ana", "ana@email.com", "clave1234", "CLIENTE");

        Mockito.when(usuarioService.emailExiste("ana@email.com")).thenReturn(false);
        Mockito.when(usuarioService.registrarUsuario(Mockito.any(Usuario.class)))
               .thenReturn(nuevoUsuario);

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoUsuario)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value("ana@email.com"))
            .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    void registrarUsuario_correoExistente_deberiaRetornar409() throws Exception {
        Usuario duplicado = new Usuario(null, "Ana", "ana@email.com", "clave1234", "CLIENTE");

        Mockito.when(usuarioService.emailExiste("ana@email.com")).thenReturn(true);

        mockMvc.perform(post("/api/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(duplicado)))
            .andExpect(status().isConflict())
            .andExpect(content().string("Ya existe un usuario con ese correo electrónico."));
    }

    @Test
    void buscarUsuario_porEmail_existente() throws Exception {
        Usuario usuario = new Usuario(1L, "Carlos", "carlos@email.com", "clave123", "CLIENTE");

        Mockito.when(usuarioService.buscarPorEmail("carlos@email.com"))
               .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/api/usuarios/buscar")
                .param("email", "carlos@email.com"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nombre").value("Carlos"))
            .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    void buscarUsuario_porEmail_noExiste() throws Exception {
        Mockito.when(usuarioService.buscarPorEmail("nadie@email.com"))
               .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/usuarios/buscar")
                .param("email", "nadie@email.com"))
            .andExpect(status().isNotFound());
    }

    @Test
    void eliminarUsuario_existente() throws Exception {
        Mockito.doNothing().when(usuarioService).eliminarUsuario(1L);

        mockMvc.perform(delete("/api/usuarios/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void obtenerTodos_losUsuarios() throws Exception {
        Usuario u1 = new Usuario(1L, "Pedro", "pedro@email.com", "clave", "ADMIN");
        Usuario u2 = new Usuario(2L, "Lucía", "lucia@email.com", "clave", "CLIENTE");

        Mockito.when(usuarioService.obtenerTodos())
               .thenReturn(Arrays.asList(u1, u2));

        mockMvc.perform(get("/api/usuarios"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].email").value("pedro@email.com"))
            .andExpect(jsonPath("$[1].nombre").value("Lucía"));
    }
}