package com.springService.orderService.service;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.springService.orderService.dto.InventoryResponse;
import com.springService.orderService.dto.OrderLineItemsDto;
import com.springService.orderService.dto.OrderRequest;
import com.springService.orderService.event.CustomEvent;
import com.springService.orderService.model.Order;
import com.springService.orderService.model.OrderLineItems;
import com.springService.orderService.reposiroty.OrderRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class OrderService {
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    WebClient.Builder webClientBuilder;
    @Autowired
    Tracer tracer;
    @Autowired
    KafkaTemplate<String, CustomEvent> kafkaTemplate;

    private final String serviceName = "OrderService";

    @Transactional
    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        ObjectMapper mapper = new JsonMapper();

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapOrderLineItemToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);

        log.info("Calling inventory service");

        for (OrderLineItems items : orderLineItems) {
            InventoryResponse res = webClientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder -> uriBuilder
                                    .queryParam("skuCode", items.getSkuCode())
                                    .queryParam("quantity", items.getQuantity())
                                    .build())
                    .retrieve()
                    .bodyToMono(InventoryResponse.class)
                    .block();
            assert res != null;
            if (res.isInStock()) {
                throw new IllegalArgumentException("Product is not in stock, please try again later");
            }
        }
        for (OrderLineItems items : orderLineItems) {
            webClientBuilder.build().put()
                    .uri("http://inventory-service/api/inventory/update",
                            uriBuilder -> uriBuilder
                                    .queryParam("skuCode", items.getSkuCode())
                                    .queryParam("quantity", items.getQuantity())
                                    .build())
                    .retrieve()
                    .toBodilessEntity()
                    .block();
        }
        orderRepository.save(order);
        CustomEvent event = new CustomEvent(serviceName, "placeOrder", order.getOrderNumber());
        kafkaTemplate.send("notificationTopic", event);
        return "Order is placed successfully";
    }

    public List<Order> findAllOrder() {
        return orderRepository.findAll();
    }

    public Order findOrderByNumber(String number) {
        Optional<Order> order = orderRepository.findByOrderNumber(number);
        if (order.isPresent()) {
            return order.get();
        }
        throw new NotFoundException();
    }


    private OrderLineItems mapOrderLineItemToDto(OrderLineItemsDto orderLineItemsDto) {
        return OrderLineItems.builder()
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();
    }
}
