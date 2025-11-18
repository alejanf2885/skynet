package com.alejanf.skynet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Document(collection = "products")
public class Product {

    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    @NotBlank
    private String categoryId; // Referencia al id de la categoría

    private String imageUrl;

    @Indexed
    private Set<String> tags = new HashSet<>(); // Evita duplicados y facilita búsqueda

    private Double rating = 0.0;
    private Integer ratingCount = 0; // Número de reseñas para cálculo de promedio

    @NotBlank
    @Indexed(unique = true)
    private String slug; // Para URLs amigables

    private boolean active = true; // Soft delete

    @Version
    private Long version; // Control de concurrencia optimista

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
