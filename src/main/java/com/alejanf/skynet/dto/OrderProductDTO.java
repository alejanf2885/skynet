package com.alejanf.skynet.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderProductDTO {

    private String productId;
    private String productName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal discount;
    private BigDecimal totalPrice;
}
