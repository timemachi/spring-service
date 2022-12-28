package com.springService.inventoryservice.service;

import com.springService.inventoryservice.dto.InventoryAskDto;
import com.springService.inventoryservice.dto.InventoryResponse;
import com.springService.inventoryservice.event.CustomEvent;
import com.springService.inventoryservice.model.Inventory;
import com.springService.inventoryservice.repository.InventoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InventoryService {

    @Autowired
    InventoryRepository inventoryRepository;
    @Autowired
    KafkaTemplate<String, CustomEvent> kafkaTemplate;

    private final String serviceName = "InventoryService";
    @Transactional(readOnly = true)
    public InventoryResponse isInStock(InventoryAskDto inventoryAskDto) {
        Optional<Inventory> inventory = inventoryRepository.findBySkuCode(inventoryAskDto.getSkuCode());
        boolean isInStock = inventory.isPresent() && inventory.get().getQuantity() >= inventoryAskDto.getQuantity();
        return InventoryResponse.builder()
                .skuCode(inventoryAskDto.getSkuCode())
                .isInStock(isInStock)
                .build();
    }
    public List<Inventory> getAll() {
        return inventoryRepository.findAll();
    }

    public void update(InventoryAskDto updateInfo) {
        Optional<Inventory> inventory = inventoryRepository.findBySkuCode(updateInfo.getSkuCode().toLowerCase());
        Inventory curr;
        curr = inventory.orElseGet(() -> Inventory.builder()
                .skuCode(updateInfo.getSkuCode())
                .quantity(0)
                .build());
        if (updateInfo.getQuantity() + curr.getQuantity() < 0) {
            throw new IllegalArgumentException(String.format("%s's stock is %d, but require %d", curr.getSkuCode(), curr.getQuantity(), updateInfo.getQuantity()));
        }
        curr.setQuantity(curr.getQuantity() + updateInfo.getQuantity());
        inventoryRepository.save(curr);
        CustomEvent event = new CustomEvent(serviceName, "updateInventory",
                updateInfo.getSkuCode() + " " + updateInfo.getQuantity());
        kafkaTemplate.send("notificationTopic", event);
    }
}
