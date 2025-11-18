package com.alejanf.skynet.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "orders")
public class Order {

    @Id
    private String id;

    private LocalDateTime orderDate = LocalDateTime.now();

    @NotEmpty(message = "Order must contain at least one product")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    private OrderStatus status = OrderStatus.PENDING;

    private PaymentMethod paymentMethod;

    @NotNull(message = "Delivery address cannot be null")
    private Address deliveryAddress;

    private String userId; // Referencia al usuario

    private boolean active = true; // Soft delete

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Calcula el total de la orden dinámicamente
    public BigDecimal getTotalPrice() {
        return orderProducts.stream()
                .map(OrderProduct::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Calcula la cantidad total de productos en la orden
    public int getTotalQuantity() {
        return orderProducts.stream()
                .mapToInt(OrderProduct::getQuantity)
                .sum();
    }
}
