package com.alejanf.skynet.dto;

import com.alejanf.skynet.model.Address;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDTO {

    private String id;
    private LocalDateTime orderDate;
    private List<OrderProductDTO> orderProducts;
    private String status;
    private String paymentMethod;
    private String userId;
    private Address deliveryAddress;

    private BigDecimal totalPrice;      // Calculado en el service
    private int totalQuantity;          // Calculado en el service
    private boolean active;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
