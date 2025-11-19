package com.alejanf.skynet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CategoryDTO {

    private String id;
    private String name;
    private String slug;
    private String description;
    private String imageUrl;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
