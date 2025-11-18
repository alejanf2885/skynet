package com.alejanf.skynet.model;

import lombok.Data;

@Data
public class OrderProduct {
    private String productId;
    private String productName;
    private Double price;
    private Integer quantity;

    private Double discount;
    private Double totalPrice;
}
