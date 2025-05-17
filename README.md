# EcoMarket SPA - Microservicio de Usuarios y Autenticación

Este repositorio contiene el microservicio de **Usuarios y Autenticación** para el sistema EcoMarket SPA del proyecto semestral FullStack_1. Permite registrar usuarios, autenticar mediante JWT, y proteger rutas según roles como `ADMIN`, `CLIENTE`, etc.

---

## Tecnologías utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JJWT (Java JWT 0.9.1)
- MySQL (XAMPP)
- Lombok
- Maven

---

## Configuración del entorno

### Base de datos

- Motor: MySQL (MariaDB compatible)
- Nombre: `inventario_db`
- Usuario: `root`
- Contraseña: *(vacía por defecto en XAMPP)*

### application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/usuarios_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8081
```

---

## Endpoints

### Públicos

| Método | Ruta                         | Descripción             |
|--------|------------------------------|--------------------------|
| POST   | `/api/usuarios/registro`     | Registrar usuario       |
| POST   | `/auth/login`                | Login y obtener token   |

### Protegidos (requieren token JWT)

| Método | Ruta                         | Rol requerido |
|--------|------------------------------|----------------|
| GET    | `/api/usuarios`              | ADMIN          |
| GET    | `/api/usuarios/buscar`       | ADMIN          |
| DELETE | `/api/usuarios/{id}`         | ADMIN          |

---

## Seguridad

- Token JWT generado con `jjwt` y firmado con clave secreta.
- El token incluye el rol del usuario (`ADMIN`, `CLIENTE`, etc.).
- Filtro personalizado (`JwtFilter`) que valida el token.
- Spring Security protege rutas según el rol (`hasRole("ADMIN")`).

---

## Cómo probar con Postman

Importa la colección incluida :  
**`EcoMarket - Usuario-Auth-Login JWT.postman_collection.JSON`**  

1. **Registrar usuario:**
   - `POST /api/usuarios/registro`
   - Body:
     ```json
     {
       "nombre": "Admin",
       "email": "admin@example.com",
       "password": "admin123",
       "rol": "ADMIN"
     }
     ```

2. **Login:**
   - `POST /auth/login`
   - Body:
     ```json
     {
       "email": "admin@example.com",
       "password": "admin123"
     }
     ```

3. **Copiar token del login y usarlo en headers:**

   ```
   Authorization: Bearer TU_TOKEN
   ```

4. **Probar endpoints protegidos con ese token**

---
