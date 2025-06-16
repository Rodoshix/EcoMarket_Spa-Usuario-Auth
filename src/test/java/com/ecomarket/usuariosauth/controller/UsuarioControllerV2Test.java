package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.assembler.UsuarioAssembler;
import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.services.UsuarioService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.singletonList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsuarioControllerV2.class)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerV2Test {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioAssembler usuarioAssembler;


    @Test
    void obtenerPorId_deberiaRetornarUsuarioConLinks() throws Exception {
        Usuario usuario = new Usuario(1L, "Luis", "luis@email.com", "clave123", "CLIENTE");
        EntityModel<Usuario> model = EntityModel.of(usuario);

        when(usuarioService.obtenerPorId(1L)).thenReturn(usuario);
        when(usuarioAssembler.toModel(usuario)).thenReturn(model);

        mockMvc.perform(get("/api/v2/usuarios/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("luis@email.com"))
                .andExpect(jsonPath("$.rol").value("CLIENTE"));
    }

    @Test
    void listarUsuarios_deberiaRetornarListaConLinks() throws Exception {
        Usuario u = new Usuario(2L, "Laura", "laura@email.com", "pass", "CLIENTE");
        EntityModel<Usuario> model = EntityModel.of(u);
        CollectionModel<EntityModel<Usuario>> modelList = CollectionModel.of(singletonList(model));

        when(usuarioService.obtenerTodos()).thenReturn(List.of(u));
        when(usuarioAssembler.toModel(any(Usuario.class))).thenReturn(model);

        mockMvc.perform(get("/api/v2/usuarios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioList[0].email").value("laura@email.com"));
    }
}