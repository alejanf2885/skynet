package com.alejanf.skynet.service;

import com.alejanf.skynet.dto.AuthResponse;
import com.alejanf.skynet.dto.LoginRequest;
import com.alejanf.skynet.dto.RegisterRequest;
import com.alejanf.skynet.model.Role;
import com.alejanf.skynet.model.User;
import com.alejanf.skynet.repository.UserRepository;
import com.alejanf.skynet.security.JwtService;
import com.alejanf.skynet.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio de autenticación que maneja el registro y login de usuarios.
 * 
 * Buenas prácticas:
 * - Validación de credenciales
 * - Encriptación de contraseñas con BCrypt
 * - Generación de tokens JWT
 * - Manejo de intentos fallidos de login
 * - Actualización de lastLogin
 */
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Registra un nuevo usuario en el sistema.
     * 
     * @param request Datos del usuario a registrar
     * @return Respuesta con el token JWT
     */
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // Verificar si el usuario ya existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + request.getEmail() + " already exists");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        user.setPhone(request.getPhone());
        user.setRole(Role.USER); // Por defecto es USER
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);

        // Guardar usuario
        user = userRepository.save(user);

        // Crear UserPrincipal para generar el token
        UserPrincipal userPrincipal = new UserPrincipal(user);

        // Generar token con el rol incluido en los claims
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        String token = jwtService.generateTokenWithExtraClaims(userPrincipal, extraClaims);
        String refreshToken = jwtService.generateRefreshToken(userPrincipal);

        // Construir respuesta
        return AuthResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }

    /**
     * Autentica un usuario y genera un token JWT.
     * 
     * @param request Credenciales de login
     * @return Respuesta con el token JWT
     */
    @Transactional
    public AuthResponse login(LoginRequest request) {
        try {
            // Autenticar usando Spring Security
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            // Si la autenticación es exitosa, cargar el usuario
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            // Verificar que el usuario esté activo
            if (!user.isActive()) {
                throw new BadCredentialsException("User account is disabled");
            }

            // Resetear intentos fallidos y actualizar lastLogin
            user.setFailedLoginAttempts(0);
            user.setLastLogin(LocalDateTime.now());
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            // Crear UserPrincipal
            UserPrincipal userPrincipal = new UserPrincipal(user);

            // Generar tokens
            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("role", user.getRole().name());
            String token = jwtService.generateTokenWithExtraClaims(userPrincipal, extraClaims);
            String refreshToken = jwtService.generateRefreshToken(userPrincipal);

            // Construir respuesta
            return AuthResponse.builder()
                    .token(token)
                    .refreshToken(refreshToken)
                    .id(user.getId())
                    .email(user.getEmail())
                    .name(user.getName())
                    .role(user.getRole())
                    .build();

        } catch (BadCredentialsException e) {
            // Incrementar intentos fallidos
            userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
                int attempts = user.getFailedLoginAttempts() == null ? 0 : user.getFailedLoginAttempts();
                user.setFailedLoginAttempts(attempts + 1);
                user.setUpdatedAt(LocalDateTime.now());
                userRepository.save(user);
            });
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    /**
     * Refresca un token JWT usando un refresh token.
     * 
     * @param refreshToken El refresh token
     * @return Nueva respuesta con tokens actualizados
     */
    @Transactional
    public AuthResponse refreshToken(String refreshToken) {
        // Extraer el email del refresh token
        String email = jwtService.extractUsername(refreshToken);

        // Cargar el usuario
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        UserPrincipal userPrincipal = new UserPrincipal(user);

        // Validar el refresh token
        if (!jwtService.validateToken(refreshToken, userPrincipal)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        // Generar nuevos tokens
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", user.getRole().name());
        String newToken = jwtService.generateTokenWithExtraClaims(userPrincipal, extraClaims);
        String newRefreshToken = jwtService.generateRefreshToken(userPrincipal);

        return AuthResponse.builder()
                .token(newToken)
                .refreshToken(newRefreshToken)
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole())
                .build();
    }
}

