package com.alejanf.skynet.security;

import com.alejanf.skynet.model.Role;
import com.alejanf.skynet.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

/**
 * Implementación de UserDetails que adapta nuestro modelo User
 * a la interfaz requerida por Spring Security.
 * 
 * Buenas prácticas:
 * - Separación de responsabilidades (adaptador)
 * - Conversión de Role a GrantedAuthority
 * - Validación de cuenta activa
 */
@Getter
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convierte el Role del usuario a una autoridad de Spring Security
        // El prefijo "ROLE_" es una convención de Spring Security
        return Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Usamos el email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Puedes implementar lógica de expiración si es necesario
    }

    @Override
    public boolean isAccountNonLocked() {
        // Bloquea la cuenta si hay demasiados intentos fallidos
        return user.getFailedLoginAttempts() == null || user.getFailedLoginAttempts() < 5;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Puedes implementar lógica de expiración de credenciales
    }

    @Override
    public boolean isEnabled() {
        return user.isActive(); // Usa el campo active del modelo User
    }

    public Role getRole() {
        return user.getRole();
    }

    public String getId() {
        return user.getId();
    }
}

