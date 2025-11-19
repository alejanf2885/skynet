package com.alejanf.skynet.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateOrderProductDTO {

    @NotNull
    private String productId;

    @NotNull
    @Positive
    private Integer quantity;

    @DecimalMin("0.0")
    private BigDecimal discount = BigDecimal.ZERO; // Opcional, normalmente viene del cupón
}
