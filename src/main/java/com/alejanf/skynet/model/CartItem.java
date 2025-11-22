package com.alejanf.skynet.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CartItem {

    @NotNull
    private String productId;

    @NotNull
    private String variantId;

    @NotNull
    private String productName;

    @NotNull
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer quantity;

    // Calcula total dinámicamente
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity));
    }
}
