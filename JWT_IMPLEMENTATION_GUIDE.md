# Guía de Implementación JWT con Autenticación Basada en Roles

## 📋 Resumen

Esta guía explica paso a paso cómo se implementó JWT (JSON Web Tokens) en tu aplicación Spring Boot con autenticación basada en roles (USER y ADMIN).

## 🏗️ Arquitectura de la Solución

### Componentes Principales

1. **JwtService** - Genera y valida tokens JWT
2. **JwtAuthenticationFilter** - Intercepta peticiones HTTP y valida tokens
3. **CustomUserDetailsService** - Carga usuarios desde MongoDB
4. **UserPrincipal** - Adapta el modelo User a UserDetails de Spring Security
5. **SecurityConfig** - Configuración de Spring Security
6. **AuthService** - Lógica de negocio para autenticación
7. **AuthController** - Endpoints REST para login y registro

## 📝 Paso a Paso de la Implementación

### Paso 1: Dependencias JWT

Se agregaron las dependencias de JJWT (Java JWT) al `pom.xml`:

```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.5</version>
</dependency>
```

**¿Por qué esta versión?**
- Versión 0.12.x es compatible con Spring Boot 3.x
- Usa APIs modernas y seguras
- Soporte completo para Java 21

### Paso 2: Configuración de Propiedades

Se agregaron propiedades en `application.properties`:

```properties
jwt.secret=your-secret-key-should-be-at-least-256-bits-long
jwt.expiration=86400000  # 24 horas en milisegundos
jwt.refresh-expiration=604800000  # 7 días en milisegundos
```

**Buenas prácticas:**
- El secret debe tener al menos 256 bits (32 caracteres) para HS256
- En producción, usa variables de entorno o un secret manager
- Expiration corto para access tokens, largo para refresh tokens

### Paso 3: Servicio JWT (JwtService)

**Responsabilidades:**
- Generar tokens JWT con claims (datos del usuario)
- Validar tokens JWT
- Extraer información de tokens (email, rol, etc.)

**Características clave:**
- Usa `SecretKey` para firmar tokens de forma segura
- Incluye el rol del usuario en los claims del token
- Valida expiración automáticamente

### Paso 4: UserDetailsService Personalizado

**CustomUserDetailsService** carga usuarios desde MongoDB y los adapta a `UserDetails`.

**UserPrincipal** es un adaptador que:
- Convierte `Role` enum a `GrantedAuthority` de Spring Security
- Implementa validaciones de cuenta (activa, bloqueada, etc.)
- Usa el email como username

### Paso 5: Filtro JWT

**JwtAuthenticationFilter** se ejecuta en cada petición HTTP:

1. Extrae el token del header `Authorization: Bearer <token>`
2. Valida el token
3. Carga el usuario desde la base de datos
4. Establece la autenticación en el contexto de Spring Security

**Flujo:**
```
Request → JwtAuthenticationFilter → Validar Token → Cargar Usuario → Establecer Autenticación → Continuar
```

### Paso 6: Configuración de Spring Security

**SecurityConfig** define:

- **Endpoints públicos:** `/api/auth/**` (login, registro)
- **Endpoints protegidos:** Todos los demás requieren autenticación
- **Endpoints de admin:** `/api/admin/**` requieren rol ADMIN
- **CORS:** Configurado para permitir peticiones del frontend
- **Sesiones:** Stateless (sin sesiones, solo JWT)

### Paso 7: Servicio y Controlador de Autenticación

**AuthService** maneja:
- **Registro:** Crea usuarios, encripta contraseñas, genera tokens
- **Login:** Valida credenciales, genera tokens, actualiza lastLogin
- **Refresh Token:** Renueva tokens expirados

**AuthController** expone endpoints REST:
- `POST /api/auth/register` - Registrar nuevo usuario
- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/refresh` - Refrescar token

## 🔐 Cómo Funciona la Autenticación Basada en Roles

### 1. Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

**Respuesta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": "user-id",
  "email": "user@example.com",
  "name": "John Doe",
  "role": "USER"
}
```

### 2. Usar el Token

En cada petición protegida, incluye el token:

```http
GET /api/user/profile
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### 3. Proteger Endpoints por Rol

**Opción 1: En SecurityConfig (a nivel de URL)**
```java
.requestMatchers("/api/admin/**").hasRole("ADMIN")
```

**Opción 2: En el controlador (más flexible)**
```java
@PreAuthorize("hasRole('ADMIN')")
@GetMapping("/admin/dashboard")
public ResponseEntity<?> adminEndpoint() {
    // Solo usuarios con rol ADMIN pueden acceder
}
```

**Opción 3: Múltiples roles**
```java
@PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
```

## 🎯 Buenas Prácticas Implementadas

### Seguridad

1. **Contraseñas encriptadas:** BCrypt con salt automático
2. **Tokens firmados:** HMAC-SHA256 con clave secreta
3. **Validación de expiración:** Tokens expiran automáticamente
4. **Refresh tokens:** Permiten renovar tokens sin re-login
5. **Intentos fallidos:** Se registran para prevenir ataques de fuerza bruta

### Arquitectura

1. **Separación de responsabilidades:** Cada clase tiene una función específica
2. **Inyección de dependencias:** Uso de `@RequiredArgsConstructor` de Lombok
3. **Validación de datos:** `@Valid` en DTOs
4. **Manejo de errores:** Excepciones apropiadas con mensajes claros

### Código

1. **Documentación:** JavaDoc en todas las clases importantes
2. **Nombres descriptivos:** Variables y métodos con nombres claros
3. **Configuración externa:** Propiedades en `application.properties`
4. **Transacciones:** `@Transactional` para operaciones de base de datos

## 📊 Flujo Completo de Autenticación

```
1. Usuario envía credenciales → POST /api/auth/login
2. AuthService valida credenciales con AuthenticationManager
3. Si válido, genera token JWT con claims (email, rol)
4. Usuario recibe token en respuesta
5. Usuario envía petición protegida con header Authorization
6. JwtAuthenticationFilter intercepta la petición
7. Extrae y valida el token
8. Carga el usuario desde MongoDB
9. Establece autenticación en SecurityContext
10. Spring Security verifica roles con @PreAuthorize
11. Si autorizado, ejecuta el endpoint
```

## 🧪 Ejemplos de Uso

### Endpoint Público (sin autenticación)
```java
// Ya configurado en SecurityConfig
.requestMatchers("/api/auth/**").permitAll()
```

### Endpoint para Usuarios Autenticados
```java
@GetMapping("/api/user/profile")
@PreAuthorize("isAuthenticated()")
public ResponseEntity<?> getProfile(@AuthenticationPrincipal UserPrincipal user) {
    // Cualquier usuario autenticado puede acceder
}
```

### Endpoint Solo para ADMIN
```java
@GetMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> getAllUsers() {
    // Solo ADMIN puede acceder
}
```

### Endpoint para Múltiples Roles
```java
@GetMapping("/api/support/tickets")
@PreAuthorize("hasAnyRole('ADMIN', 'SUPPORT')")
public ResponseEntity<?> getTickets() {
    // ADMIN o SUPPORT pueden acceder
}
```

## 🔧 Configuración Adicional Recomendada

### Variables de Entorno (Producción)

En lugar de hardcodear el secret en `application.properties`, usa variables de entorno:

```properties
jwt.secret=${JWT_SECRET:default-secret-for-development-only}
```

### Logging

Agrega logging para debugging:

```java
private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
logger.debug("Validating token for user: {}", userEmail);
```

### Rate Limiting

Considera agregar rate limiting para prevenir ataques:
- Spring Security tiene soporte para esto
- O usa bibliotecas como Bucket4j

### HTTPS

En producción, siempre usa HTTPS para proteger los tokens en tránsito.

## 🚀 Próximos Pasos

1. **Agregar más roles** si es necesario (ya tienes USER y ADMIN)
2. **Implementar logout** (invalidar tokens en una blacklist)
3. **Agregar tests unitarios** para los servicios
4. **Implementar recuperación de contraseña**
5. **Agregar verificación de email**

## 📚 Recursos Adicionales

- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/index.html)
- [JWT.io](https://jwt.io/) - Para debuggear tokens JWT
- [JJWT Documentation](https://github.com/jwtk/jjwt)

---

**Nota:** El archivo `ExampleProtectedController.java` es solo para demostración. Puedes eliminarlo cuando crees tus propios controladores protegidos.

