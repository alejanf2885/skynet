package com.alejanf.skynet.repository;

import com.alejanf.skynet.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepository extends MongoRepository<Order, String> {
}
