package com.springService.inventoryservice.controller;

import com.springService.inventoryservice.dto.InventoryAskDto;
import com.springService.inventoryservice.dto.InventoryResponse;
import com.springService.inventoryservice.model.Inventory;
import com.springService.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    @Autowired
    InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public InventoryResponse isInStock(@RequestParam String skuCode, @RequestParam String quantity) {
        InventoryAskDto skuInfo = InventoryAskDto.builder().skuCode(skuCode).quantity(Integer.parseInt(quantity)).build();
        return inventoryService.isInStock(skuInfo);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public void updateStock(@RequestParam String skuCode, @RequestParam String quantity) {
        InventoryAskDto updateInfo = InventoryAskDto.builder().skuCode(skuCode).quantity(Integer.parseInt(quantity)).build();
        inventoryService.update(updateInfo);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getAll() {
        return inventoryService.getAll();
    }


}
