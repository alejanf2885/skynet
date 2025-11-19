package com.alejanf.skynet.repository;

import com.alejanf.skynet.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {

   Optional<Product> findBySlug(String slug);
}
