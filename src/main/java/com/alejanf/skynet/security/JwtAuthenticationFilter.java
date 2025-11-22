package com.alejanf.skynet.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro JWT que intercepta cada petición HTTP y valida el token JWT.
 * 
 * Flujo:
 * 1. Extrae el token del header Authorization
 * 2. Valida el token
 * 3. Carga el usuario desde la base de datos
 * 4. Establece la autenticación en el contexto de seguridad
 * 
 * Buenas prácticas:
 * - Extiende OncePerRequestFilter para ejecutarse una vez por petición
 * - No bloquea peticiones sin token (permite endpoints públicos)
 * - Valida el token antes de cargar el usuario (optimización)
 * - Limpia el contexto de seguridad si el token es inválido
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    /**
     * Método principal del filtro que se ejecuta en cada petición.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        // 1. Extraer el token del header Authorization
        final String authHeader = request.getHeader("Authorization");
        
        // Si no hay header Authorization o no empieza con "Bearer ", continuar con el filtro
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. Extraer el token (remover el prefijo "Bearer ")
            final String jwt = authHeader.substring(7);
            
            // 3. Extraer el email del token
            final String userEmail = jwtService.extractUsername(jwt);

            // 4. Si hay un email y no hay autenticación en el contexto actual
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // 5. Cargar el usuario desde la base de datos
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // 6. Validar el token
                if (jwtService.validateToken(jwt, userDetails)) {
                    
                    // 7. Crear el objeto de autenticación
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    
                    // 8. Agregar detalles de la petición (IP, sesión, etc.)
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    
                    // 9. Establecer la autenticación en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // En caso de error, limpiar el contexto de seguridad
            SecurityContextHolder.clearContext();
            // Log del error (puedes usar un logger aquí)
            // logger.error("Cannot set user authentication: {}", e);
        }

        // 10. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}

