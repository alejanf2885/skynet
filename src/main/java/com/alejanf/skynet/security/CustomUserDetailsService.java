package com.alejanf.skynet.security;

import com.alejanf.skynet.model.User;
import com.alejanf.skynet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio personalizado que implementa UserDetailsService de Spring Security.
 * Carga usuarios desde MongoDB y los adapta a UserDetails.
 * 
 * Buenas prácticas:
 * - Implementación de la interfaz estándar de Spring Security
 * - Manejo de excepciones apropiado
 * - Uso de transacciones para operaciones de base de datos
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Carga un usuario por su email (username) desde la base de datos.
     * 
     * @param email El email del usuario (usado como username)
     * @return UserDetails con la información del usuario
     * @throws UsernameNotFoundException Si el usuario no existe
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserPrincipal(user);
    }

    /**
     * Carga un usuario por su ID.
     * Útil para operaciones que requieren el usuario completo.
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));

        return new UserPrincipal(user);
    }
}

