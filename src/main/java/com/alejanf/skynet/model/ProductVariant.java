package com.alejanf.skynet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document(collection = "product_variants") // Nombre de la colección en MongoDB
public class ProductVariant {

    @Id
    private String id;

    @NotBlank
    private String productId; // referencia al producto padre

    @NotBlank
    private String size;

    private String color; // opcional

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @PositiveOrZero
    private Integer stock;

    private String imageUrl;

    private String sku;
}