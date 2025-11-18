package com.alejanf.skynet.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    @Size(min = 1, max = 50)
    @NotBlank
    private String name;

    @NotBlank
    @Indexed(unique = true)
    private String slug; // Para URLs amigables

    @NotBlank
    @Size(min = 1, max = 100)
    private String description;

    @URL
    private String imageUrl;

    private boolean active = true;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();


}