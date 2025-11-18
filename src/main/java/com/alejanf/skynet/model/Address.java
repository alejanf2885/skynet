package com.alejanf.skynet.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Address {
    @NotBlank
    private String street;

    private String street2;

    @NotBlank
    private String city;

    @NotBlank
    private String postalCode;

    @NotBlank
    private String country;
}
