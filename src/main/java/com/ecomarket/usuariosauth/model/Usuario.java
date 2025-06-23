package com.ecomarket.usuariosauth.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "usuarios")
@Schema(description = "Entidad que representa a un usuario del sistema EcoMarket SPA")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del usuario", example = "1")
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre no debe superar los 100 caracteres")
    @Schema(description = "Nombre completo del usuario", example = "Ana González", maxLength = 100)
    private String nombre;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no es válido")
    @Size(max = 100, message = "El correo no debe superar los 100 caracteres")
    @Column(unique = true)
    @Schema(description = "Correo electrónico del usuario. Debe ser único", example = "ana@correo.cl", maxLength = 100)
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @Schema(description = "Contraseña de acceso del usuario", example = "secreta123", minLength = 8, maxLength = 100)
    private String password;

    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 50, message = "El rol no debe superar los 50 caracteres")
    @Schema(description = "Rol del usuario en el sistema", example = "CLIENTE", maxLength = 50)
    private String rol;  // Ej: "ADMIN", "CLIENTE", "VENDEDOR"
}