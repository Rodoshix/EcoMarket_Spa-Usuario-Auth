package com.ecomarket.usuariosauth.controller;

import com.ecomarket.usuariosauth.assembler.UsuarioAssembler;
import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.services.UsuarioService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v2/usuarios")
@Tag(name = "Usuarios v2", description = "Versión enriquecida con HATEOAS para gestión de usuarios")
public class UsuarioControllerV2 {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UsuarioAssembler usuarioAssembler;

    @GetMapping("/{id}")
    public EntityModel<Usuario> obtenerPorId(@PathVariable Long id) {
        Usuario usuario = usuarioService.obtenerPorId(id);
        return usuarioAssembler.toModel(usuario);
    }

    @GetMapping
    public CollectionModel<EntityModel<Usuario>> listarUsuarios() {
        List<Usuario> usuarios = (List<Usuario>) usuarioService.obtenerTodos();

        List<EntityModel<Usuario>> modelos = usuarios.stream()
                .map(usuarioAssembler::toModel)
                .toList();

        return CollectionModel.of(modelos,
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withSelfRel());
    }
}