package com.springService.productservice.service;

import com.springService.productservice.dto.ProductRequest;
import com.springService.productservice.dto.ProductResponse;
import com.springService.productservice.event.CustomEvent;
import com.springService.productservice.model.Product;
import com.springService.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    KafkaTemplate<String, CustomEvent> kafkaTemplate;

    private final String serviceName = "ProductService";

    public void createProduct(ProductRequest productRequest) {
        if (isNameExist(productRequest.getName())) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), String.format("%s is already exist", productRequest.getName()));
        }

        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        product = productRepository.save(product);
        log.info("Product {} is saved", product.getId());
        CustomEvent event = new CustomEvent(serviceName, "createProduct", product.getName());
        kafkaTemplate.send("notificationTopic", event);
    }

    public ProductResponse findProductByName(String name) {
        Optional<Product> productOptional = productRepository.findByNameIgnoreCase(name);
        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), String.format("Product %s is not exist", name));
        }
        return mapToProductResponse(productOptional.get());
    }

    public void deleteProductByName(String name) {
        Optional<Product> productOptional = productRepository.findByNameIgnoreCase(name);
        if (productOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), String.format("Product %s is not exist", name));
        }
        productRepository.delete(productOptional.get());
        log.info("Product {} is deleted", name);
        CustomEvent event = new CustomEvent(serviceName, "deleteProduct", productOptional.get().getName());
        kafkaTemplate.send("notificationTopic", event);
    }

    private boolean isNameExist(String name) {
        return productRepository.findByNameIgnoreCase(name).isPresent();
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        
        return products.stream().map(this::mapToProductResponse).toList();
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
