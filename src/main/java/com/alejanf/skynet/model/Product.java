package com.alejanf.skynet.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "products")
public class Product {

    @Id
    private String id;

    private String name;
    private String description;
    private Double price;
    private Integer stock;
    private String categoryId;

    private String imageUrl;
    private List<String> tags = new ArrayList<>();
    private Double rating;

}
