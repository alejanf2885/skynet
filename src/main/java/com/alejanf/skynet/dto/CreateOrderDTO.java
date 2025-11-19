package com.alejanf.skynet.dto;

import com.alejanf.skynet.model.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CreateOrderDTO {

    @NotEmpty(message = "Order must contain at least one product")
    private List<CreateOrderProductDTO> orderProducts;

    @NotNull(message = "Delivery address cannot be null")
    private Address deliveryAddress;

    private String paymentMethod; // Puede ser ENUM en backend

    private String notes; // Opcional
}
