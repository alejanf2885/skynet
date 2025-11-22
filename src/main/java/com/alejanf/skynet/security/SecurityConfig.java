package com.alejanf.skynet.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * Configuración principal de Spring Security.
 * 
 * Buenas prácticas implementadas:
 * - Deshabilitación de sesiones (stateless con JWT)
 * - Configuración de CORS para APIs REST
 * - Separación de endpoints públicos y privados
 * - Autorización basada en roles con @EnableMethodSecurity
 * - Uso de BCrypt para encriptación de contraseñas
 * - Integración del filtro JWT en la cadena de seguridad
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // Habilita @PreAuthorize y @PostAuthorize
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;

    /**
     * Define la cadena de filtros de seguridad y las reglas de autorización.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Deshabilitar CSRF porque usamos JWT (stateless)
            .csrf(AbstractHttpConfigurer::disable)
            
            // Configurar CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            
            // Configurar autorización de endpoints
            .authorizeHttpRequests(auth -> auth
                // Endpoints públicos (sin autenticación)
                .requestMatchers(
                    "/api/auth/**",           // Endpoints de autenticación
                    "/api/public/**",         // Endpoints públicos
                    "/error",                 // Endpoints de error
                    "/swagger-ui/**",          // Swagger UI (si lo usas)
                    "/v3/api-docs/**"         // Swagger docs (si lo usas)
                ).permitAll()
                
                // Endpoints que requieren rol ADMIN
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                
                // Todos los demás endpoints requieren autenticación
                .anyRequest().authenticated()
            )
            
            // Configurar el proveedor de autenticación
            .authenticationProvider(authenticationProvider())
            
            // Agregar el filtro JWT antes del filtro de autenticación por defecto
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            
            // Configurar la política de sesiones (stateless porque usamos JWT)
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    /**
     * Configuración de CORS para permitir peticiones desde el frontend.
     * Ajusta los orígenes permitidos según tu frontend.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Orígenes permitidos (ajusta según tu frontend)
        configuration.setAllowedOrigins(List.of(
            "http://localhost:3000",  // React por defecto
            "http://localhost:4200",  // Angular por defecto
            "http://localhost:5173"    // Vite por defecto
        ));
        
        // Métodos HTTP permitidos
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        
        // Headers permitidos
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization",
            "Content-Type",
            "X-Requested-With"
        ));
        
        // Permitir credenciales (cookies, headers de autenticación)
        configuration.setAllowCredentials(true);
        
        // Headers expuestos al frontend
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        // Tiempo de cache para preflight requests
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }

    /**
     * Proveedor de autenticación que usa nuestro UserDetailsService.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Codificador de contraseñas usando BCrypt.
     * BCrypt es un algoritmo de hash seguro y ampliamente usado.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * AuthenticationManager necesario para el proceso de autenticación.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

