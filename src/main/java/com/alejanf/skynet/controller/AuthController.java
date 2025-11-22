package com.alejanf.skynet.controller;

import com.alejanf.skynet.dto.AuthResponse;
import com.alejanf.skynet.dto.LoginRequest;
import com.alejanf.skynet.dto.RegisterRequest;
import com.alejanf.skynet.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST para manejar la autenticación de usuarios.
 * 
 * Endpoints:
 * - POST /api/auth/register - Registro de nuevos usuarios
 * - POST /api/auth/login - Login de usuarios existentes
 * - POST /api/auth/refresh - Refrescar token JWT
 * 
 * Buenas prácticas:
 * - Validación de DTOs con @Valid
 * - Manejo de respuestas HTTP apropiadas
 * - Separación de responsabilidades (lógica en el servicio)
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Endpoint para registrar un nuevo usuario.
     * 
     * @param request Datos del usuario a registrar
     * @return Respuesta con el token JWT
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para autenticar un usuario existente.
     * 
     * @param request Credenciales de login
     * @return Respuesta con el token JWT
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para refrescar un token JWT.
     * 
     * @param refreshToken El refresh token
     * @return Nueva respuesta con tokens actualizados
     */
    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody String refreshToken) {
        AuthResponse response = authService.refreshToken(refreshToken);
        return ResponseEntity.ok(response);
    }
}

