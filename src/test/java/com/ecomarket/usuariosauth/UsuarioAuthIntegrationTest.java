package com.ecomarket.usuariosauth;

import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UsuarioAuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port;
        usuarioRepository.deleteAll();
    }

    @Test
    void registroUsuario_exitoso_deberiaRetornar200() {
        Usuario usuario = new Usuario(null, "Mario", "mario@correo.cl", "clave1234", "CLIENTE");

        ResponseEntity<Usuario> response = restTemplate.postForEntity(
                baseUrl + "/api/usuarios/registro",
                usuario,
                Usuario.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo("mario@correo.cl");
    }

    @Test
    void login_exitoso_deberiaRetornarToken() {
        Usuario usuario = new Usuario(null, "Luisa", "luisa@correo.cl", "mipass123", "CLIENTE");
        usuarioRepository.save(usuario);

        String json = """
            {
              "email": "luisa@correo.cl",
              "password": "mipass123"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl + "/auth/login", request, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("token");
    }
}