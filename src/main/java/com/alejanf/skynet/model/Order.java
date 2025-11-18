package com.alejanf.skynet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "orders")
@Data
public class Order {

    @Id
    private String id;

    private LocalDateTime orderDate;
    private Double totalPrice;

    private List<OrderProduct> orderProducts;
    private String status;

    private String paymentMethod;
    private Address deliveryAddress;
    private String notes;
}
