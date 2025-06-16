package com.ecomarket.usuariosauth.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Microservicio Usuario & Autenticación - EcoMarket SPA")
                .version("1.0.0")
                .description("Este microservicio gestiona la autenticación de usuarios y el manejo de cuentas para el sistema EcoMarket SPA.")
                .contact(new Contact()
                    .name("Equipo de Desarrollo EcoMarket")
                    .email("soporte@ecomarket.cl")
                    .url("https://www.ecomarket.cl")
                )
            );
    }
}