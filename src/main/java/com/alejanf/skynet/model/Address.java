package com.alejanf.skynet.model;

import lombok.Data;

@Data
public class Address {
    private String street;
    private String city;
    private String postalCode;
    private String country;
}
