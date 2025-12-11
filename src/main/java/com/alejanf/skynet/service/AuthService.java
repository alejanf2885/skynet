package com.alejanf.skynet.service;

import com.alejanf.skynet.dto.AuthResponseDTO;
import com.alejanf.skynet.dto.LoginRequestDTO;
import com.alejanf.skynet.dto.RegisterRequestDTO;
import com.alejanf.skynet.dto.UserDTO;
import com.alejanf.skynet.jwt.JwtTokenProvider;
import com.alejanf.skynet.model.Role;
import com.alejanf.skynet.model.User;
import com.alejanf.skynet.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthResponseDTO register(RegisterRequestDTO request, HttpServletResponse response) {
        // Verificar si el email ya existe
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Crear nuevo usuario
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone());
        user.setRole(Role.USER);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setFailedLoginAttempts(0);

        User savedUser = userRepository.save(user);

        // Generar token JWT
        String token = jwtTokenProvider.generateToken(savedUser.getEmail(), savedUser.getId());

        // Crear cookie segura
        Cookie jwtCookie = createSecureCookie(token);
        response.addCookie(jwtCookie);

        // Actualizar lastLogin
        savedUser.setLastLogin(LocalDateTime.now());
        userRepository.save(savedUser);

        // Convertir a DTO
        UserDTO userDTO = convertToDTO(savedUser);

        return new AuthResponseDTO("User registered successfully", userDTO);
    }

    public AuthResponseDTO login(LoginRequestDTO request, HttpServletResponse response) {
        // Buscar usuario por email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        // Verificar si el usuario está activo
        if (!user.isActive()) {
            throw new RuntimeException("User account is disabled");
        }

        // Verificar contraseña
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            // Incrementar intentos fallidos
            user.setFailedLoginAttempts(user.getFailedLoginAttempts() + 1);
            userRepository.save(user);
            throw new RuntimeException("Invalid email or password");
        }

        // Resetear intentos fallidos en caso de login exitoso
        user.setFailedLoginAttempts(0);
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);

        // Generar token JWT
        String token = jwtTokenProvider.generateToken(user.getEmail(), user.getId());

        // Crear cookie segura
        Cookie jwtCookie = createSecureCookie(token);
        response.addCookie(jwtCookie);

        // Convertir a DTO
        UserDTO userDTO = convertToDTO(user);

        return new AuthResponseDTO("Login successful", userDTO);
    }

    public void logout(HttpServletResponse response) {
        // Crear una cookie vacía con el mismo nombre para eliminarla
        Cookie jwtCookie = new Cookie("jwt", null);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setSecure(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge(0);
        response.addCookie(jwtCookie);
    }

    private Cookie createSecureCookie(String token) {
        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true); // Protege contra XSS
        cookie.setSecure(true); // Solo se envía por HTTPS
        cookie.setPath("/"); // Disponible en todo el sitio
        cookie.setMaxAge((int) (86400000 / 1000)); // 24 horas en segundos
        // SameSite=None se maneja en la configuración si es necesario para cross-site
        return cookie;
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRole(user.getRole().name());
        dto.setAddresses(user.getAddresses());
        dto.setActive(user.isActive());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }
}

