package com.springService.productservice.controller;

import com.springService.productservice.dto.ProductRequest;
import com.springService.productservice.dto.ProductResponse;
import com.springService.productservice.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Autowired
    ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createProduct(@RequestBody ProductRequest productRequest) {
        productService.createProduct(productRequest);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductByName(@PathVariable String name) {
        return productService.findProductByName(name);
    }

    @DeleteMapping("/{name}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProductByName(@PathVariable String name) {
        productService.deleteProductByName(name);
    }



}
