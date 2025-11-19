package com.alejanf.skynet.repository;

import com.alejanf.skynet.model.Category;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CategoryRepository extends MongoRepository<Category, String> {
}
