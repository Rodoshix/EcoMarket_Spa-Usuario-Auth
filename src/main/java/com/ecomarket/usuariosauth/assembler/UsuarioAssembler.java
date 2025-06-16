package com.ecomarket.usuariosauth.assembler;

import com.ecomarket.usuariosauth.controller.UsuarioControllerV2;
import com.ecomarket.usuariosauth.model.Usuario;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UsuarioAssembler implements RepresentationModelAssembler<Usuario, EntityModel<Usuario>> {

    @Override
    public EntityModel<Usuario> toModel(Usuario usuario) {
        return EntityModel.of(usuario,
            linkTo(methodOn(UsuarioControllerV2.class).obtenerPorId(usuario.getId())).withSelfRel(),
            linkTo(methodOn(UsuarioControllerV2.class).listarUsuarios()).withRel("usuarios")
        );
    }
}