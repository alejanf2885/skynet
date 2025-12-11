package com.alejanf.skynet.controller;

import com.alejanf.skynet.dto.AuthResponseDTO;
import com.alejanf.skynet.dto.LoginRequestDTO;
import com.alejanf.skynet.dto.RegisterRequestDTO;
import com.alejanf.skynet.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO request,
            HttpServletResponse response) {
        try {
            AuthResponseDTO authResponse = authService.register(request, response);
            return ResponseEntity.status(HttpStatus.CREATED).body(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new AuthResponseDTO(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO request,
            HttpServletResponse response) {
        try {
            AuthResponseDTO authResponse = authService.login(request, response);
            return ResponseEntity.ok(authResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthResponseDTO(e.getMessage(), null));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<AuthResponseDTO> logout(HttpServletResponse response) {
        authService.logout(response);
        return ResponseEntity.ok(new AuthResponseDTO("Logout successful", null));
    }
}

