package com.springService.orderService.controller;

import com.springService.orderService.dto.OrderRequest;
import com.springService.orderService.model.Order;
import com.springService.orderService.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    WebClient.Builder webClientBuilder;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod="fallback")
    @TimeLimiter(name = "inventory")
    @Retry(name = "inventory")
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest orderRequest) {
        try{
            return CompletableFuture.supplyAsync(()->orderService.placeOrder(orderRequest));
        }catch (IllegalArgumentException e) {
            return CompletableFuture.supplyAsync(e::getLocalizedMessage);
        }
    }
    public CompletableFuture<String> fallback(OrderRequest orderRequest, Exception e) {
        return CompletableFuture.supplyAsync(()->"Oops, somethings went wrong, please try again later: " + e.getMessage());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.FOUND)
    public Order findOrderByOrderNumber(@PathVariable String orderNumber) {
        try {
            return orderService.findOrderByNumber(orderNumber);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/all")
    public List<Order> findAllOrder() {
        return orderService.findAllOrder();
    }
}
