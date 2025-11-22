package com.alejanf.skynet.controller;

import com.alejanf.skynet.security.UserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de ejemplo que muestra cómo proteger endpoints con roles.
 * 
 * Este es un ejemplo educativo. Puedes eliminarlo cuando crees tus propios controladores.
 * 
 * Buenas prácticas demostradas:
 * - Uso de @PreAuthorize para autorización basada en roles
 * - Inyección del usuario autenticado con @AuthenticationPrincipal
 * - Separación de endpoints por rol
 */
@RestController
@RequestMapping("/api")
public class ExampleProtectedController {

    /**
     * Endpoint accesible por cualquier usuario autenticado.
     * 
     * Uso: GET /api/user/profile
     * Headers: Authorization: Bearer <token>
     */
    @GetMapping("/user/profile")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<Map<String, Object>> getUserProfile(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> profile = new HashMap<>();
        profile.put("id", userPrincipal.getId());
        profile.put("email", userPrincipal.getUsername());
        profile.put("role", userPrincipal.getRole());
        profile.put("message", "This is your profile");
        return ResponseEntity.ok(profile);
    }

    /**
     * Endpoint accesible solo por usuarios con rol ADMIN.
     * 
     * Uso: GET /api/admin/dashboard
     * Headers: Authorization: Bearer <token>
     */
    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> getAdminDashboard(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, String> dashboard = new HashMap<>();
        dashboard.put("message", "Welcome to the admin dashboard, " + userPrincipal.getUsername());
        dashboard.put("role", userPrincipal.getRole().name());
        return ResponseEntity.ok(dashboard);
    }

    /**
     * Endpoint accesible por cualquier usuario autenticado.
     * Muestra cómo obtener información del usuario actual.
     */
    @GetMapping("/user/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", userPrincipal.getId());
        userInfo.put("email", userPrincipal.getUsername());
        userInfo.put("role", userPrincipal.getRole());
        userInfo.put("authenticated", true);
        return ResponseEntity.ok(userInfo);
    }
}

