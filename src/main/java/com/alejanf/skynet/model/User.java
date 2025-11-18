package com.alejanf.skynet.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Email
    @Indexed(unique = true)
    private String email;

    @NotBlank
    private String password; // Guardar encriptado con BCrypt

    @Pattern(regexp = "\\+?\\d{7,15}", message = "Invalid phone number")
    private String phone;

    private Role role = Role.USER;

    private List<Address> addresses = new ArrayList<>();

    // Referencias a órdenes
    private List<String> orderIds = new ArrayList<>();

    private boolean active = true; // Soft delete

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Opcional: auditoría de accesos
    private LocalDateTime lastLogin;
    private Integer failedLoginAttempts = 0;
}
