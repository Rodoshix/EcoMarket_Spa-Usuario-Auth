package com.ecomarket.usuariosauth.performance;

import com.ecomarket.usuariosauth.model.Usuario;
import com.ecomarket.usuariosauth.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class UsuarioAuthPerformanceTest {

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

        // Crear un usuario real para los logins
        Usuario usuario = new Usuario(null, "Stress Tester", "test@correo.cl", "clave123", "CLIENTE");
        usuarioRepository.save(usuario);
    }

    @Test
    void loginMasivo_100peticiones_deberianResponderRápido() {
        String json = """
            {
              "email": "test@correo.cl",
              "password": "clave123"
            }
            """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        long inicio = System.currentTimeMillis();

        int exitosos = 0;

        for (int i = 0; i < 100; i++) {
            HttpEntity<String> request = new HttpEntity<>(json, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(
                    baseUrl + "/auth/login", request, String.class
            );

            if (response.getStatusCode() == HttpStatus.OK &&
                response.getBody() != null && response.getBody().contains("token")) {
                exitosos++;
            }
        }

        long fin = System.currentTimeMillis();
        long duracion = fin - inicio;

        System.out.println("Tiempo total: " + duracion + " ms");
        System.out.println("Logins exitosos: " + exitosos);

        assertThat(exitosos).isEqualTo(100);
        assertThat(duracion).isLessThan(3000); // Esperamos que sea menor a 3 segundos
    }
}