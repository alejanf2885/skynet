package com.alejanf.skynet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
@Data
public class User {

    @Id
    private String id;

    private String name;
    private String email;
    private String password;
    private String phone;
    private String role = "USER";

    private  Address address;

    private List<Order> orders = new ArrayList<Order>();

}
