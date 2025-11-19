package com.alejanf.skynet.dto;

import com.alejanf.skynet.model.Address;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class UserDTO {

    private String id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private List<Address> addresses;

    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
