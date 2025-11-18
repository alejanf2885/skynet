package com.alejanf.skynet.model;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProduct {

    @NotNull
    private String productId;

    @NotNull
    private String productName;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotNull
    @Positive
    private Integer quantity;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal discount = BigDecimal.ZERO;


    // Calcula el total dinámicamente considerando la cantidad y el descuento
    public BigDecimal getTotalPrice() {
        return price.multiply(BigDecimal.valueOf(quantity)).subtract(discount);
    }

    //Metodo opcional para agregar impuestos
    public BigDecimal getTotalPriceWithTax(BigDecimal taxRate) {
        BigDecimal total = getTotalPrice();
        return total.add(total.multiply(taxRate));
    }
}
