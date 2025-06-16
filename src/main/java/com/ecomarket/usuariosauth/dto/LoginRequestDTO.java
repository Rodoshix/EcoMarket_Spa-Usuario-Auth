package com.ecomarket.usuariosauth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "DTO para el login de usuario. Contiene email y contraseña.")
public class LoginRequestDTO {
    @NotBlank
    @Email
    @Schema(description = "Correo electrónico del usuario", example = "cliente@email.com")
    private String email;

    @NotBlank
    @Schema(description = "Contraseña del usuario", example = "clave1234")
    private String password;
}