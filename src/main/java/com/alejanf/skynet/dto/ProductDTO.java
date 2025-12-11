package com.alejanf.skynet.dto;

import com.alejanf.skynet.model.Product;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class ProductDTO {

    private String id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer stockTotal;
    private String categoryId;
    private List<String> variantIds;
    private String imageUrl;
    private Set<String> tags;
    private Double rating;
    private Integer ratingCount;
    private String slug;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductDTO fromEntity(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStockTotal(product.getStockTotal());
        dto.setCategoryId(product.getCategoryId());
        dto.setVariantIds(product.getVariantIds());
        dto.setImageUrl(product.getImageUrl());
        dto.setTags(product.getTags());
        dto.setRating(product.getRating());
        dto.setRatingCount(product.getRatingCount());
        dto.setSlug(product.getSlug());
        dto.setActive(product.isActive());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        return dto;
    }
}

