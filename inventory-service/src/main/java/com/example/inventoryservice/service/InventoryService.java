package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryDto;
import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;

@Service
public class InventoryService {
    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public InventoryDto get(Long bookId) {
        return repo.findById(bookId).map(i -> new InventoryDto(i.getBookId(), i.getQuantityAvailable())).orElse(null);
    }

    public InventoryDto createOrUpdate(InventoryDto dto) {
        Inventory i = Inventory.builder().bookId(dto.getBookId()).quantityAvailable(dto.getQuantityAvailable()).build();
        Inventory saved = repo.save(i);
        return new InventoryDto(saved.getBookId(), saved.getQuantityAvailable());
    }

    public InventoryDto decrease(Long bookId, int amount) {
        Inventory i = repo.findById(bookId).orElse(null);
        if (i == null)
            return null;
        if (i.getQuantityAvailable() < amount)
            throw new RuntimeException("Not enough stock");
        i.setQuantityAvailable(i.getQuantityAvailable() - amount);
        Inventory saved = repo.save(i);
        return new InventoryDto(saved.getBookId(), saved.getQuantityAvailable());
    }
}
