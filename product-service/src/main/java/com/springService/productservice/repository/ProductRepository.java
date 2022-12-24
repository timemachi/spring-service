package com.springService.productservice.repository;

import com.springService.productservice.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByNameIgnoreCase(String name);
}
